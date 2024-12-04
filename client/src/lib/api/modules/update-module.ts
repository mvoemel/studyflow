import { tuam } from "@/lib/tuam";
import { Module } from "@/types";

type UpdateModuleRequestBody = Omit<
  Module,
  "id" | "userId" | "degreeId" | "semesterId"
>;

const updateModuleRequest = async (
  body: UpdateModuleRequestBody,
  moduleId: string
) => {
  return await tuam.post<void, UpdateModuleRequestBody>(
    `/api/modules/${moduleId}`,
    body
  );
};

export { type UpdateModuleRequestBody, updateModuleRequest };
