import { tuam } from "@/lib/tuam";
import { Grade, Module } from "@/types";

type UpdateGradesForModuleRequestBody = {
  id: Module["id"];
  grades: Grade[];
};

const updateGradesForModuleRequest = async (
  body: UpdateGradesForModuleRequestBody,
  degreeId: string
) => {

  const newBody = {
    id: body.id,
    grades: body.grades.map((grade) => ({
      id: grade.id,
      name: grade.name,
      value: grade.value,
      percentage: grade.percentage,
      belongsToModule: grade.moduleId,
    })),
  }
    return await tuam.post<
        void,
        {
            id: string;
            grades: {
                id: string;
                name: string;
                value: number;
                percentage: number;
                belongsToModule: string;
            }[];
        }
    >(`/api/degrees/${degreeId}/grades`, newBody);
};

export { type UpdateGradesForModuleRequestBody, updateGradesForModuleRequest };
