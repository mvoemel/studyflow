import { tuam } from "@/lib/tuam";
import { GradeViewTree } from "@/types";

type GradesForDegreeResponseData = GradeViewTree;

const getGradesForDegreeRequest = async (degreeId: string) => {
  // TODO: parse accordingly
  // GradesViewTree = GradesViewSemester[]
  // GradesViewSemester = {id: number, name: string, modules: GradesViewModule[]}[]
  // GradesViewModule = {id: number, name: string, ects: number, grades: Grade[]}[]
  // Grade = {id: number, name: string, value: number, percentage: number, ownerId: number}
  return await tuam.get<GradesForDegreeResponseData>(
    `/api/degrees/${degreeId}/grades`
  );
};

export { type GradesForDegreeResponseData, getGradesForDegreeRequest };
