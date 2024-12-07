import { tuam } from "@/lib/tuam";
import { GradeViewTree } from "@/types";

type GradesForDegreeResponseData = GradeViewTree;

const getGradesForDegreeRequest = async (degreeId: string) => {
  type GradesViewTreeFromServer = GradesViewSemesterFromServer[];
  type GradesViewSemesterFromServer = {
    id: number;
    name: string;
    modules: GradesViewModuleFromServer[];
  };
  type GradesViewModuleFromServer = {
    id: number;
    name: string;
    ects: number;
    grades: GradeFromServer[];
  };
  type GradeFromServer = {
    id: number;
    name: string;
    value: number;
    percentage: number;
    ownerId: number;
  };

  const response = await tuam.get<GradesViewTreeFromServer>(
    `/api/degrees/${degreeId}/grades`
  );

  const gradesForDegreeResponseData: GradesForDegreeResponseData = response.map(
    (semester) => ({
      semesterId: semester.id.toString(),
      semesterName: semester.name,
      modules: semester.modules.map((module) => ({
        moduleId: module.id.toString(),
        moduleName: module.name,
        moduleEcts: module.ects,
        grades: module.grades.map(
          ({ id, name, value, percentage, ownerId }) => ({
            id: id.toString(),
            name,
            value,
            percentage,
            moduleId: ownerId.toString(),
          })
        ),
      })),
    })
  );

  return gradesForDegreeResponseData;
};

export { type GradesForDegreeResponseData, getGradesForDegreeRequest };
