import { tuam } from "@/lib/tuam";
import { Grade, Module } from "@/types";

type UpdateGradesForModuleRequestBody = {
  id: Module["id"];
  grades: Omit<Grade, "moduleId">[];
};

const updateGradesForModuleRequest = async (
  body: UpdateGradesForModuleRequestBody,
  degreeId: string
) => {
  return await tuam.post<void, UpdateGradesForModuleRequestBody>(
    `/api/degrees/${degreeId}/grades`,
    body
  );
};

export { type UpdateGradesForModuleRequestBody, updateGradesForModuleRequest };
