import { tuam } from "@/lib/tuam";
import { Grade } from "@/types";

type UpdateGradesForModuleRequestBody = Omit<Grade, "moduleId">[];

const updateGradesForModuleRequest = async (
  body: UpdateGradesForModuleRequestBody,
  moduleId: string
) => {
  return await tuam.patch<void, UpdateGradesForModuleRequestBody>(
    `/api/modules/${moduleId}/grades`,
    body
  );
};

export { type UpdateGradesForModuleRequestBody, updateGradesForModuleRequest };
