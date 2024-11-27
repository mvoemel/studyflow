import { Module } from "@/types";
import { tuam } from "../tuam";

type NewModuleRequestBody = Omit<Module, "id" | "userId">;
const newModuleRequest = async (body: NewModuleRequestBody) => {
  return await tuam.post<void, NewModuleRequestBody>("/api/modules", body);
};

type AllModulesResponseData = Module[];
const getAllModules = async (degreeId?: string, semesterId?: string) => {
  return await tuam.get<AllModulesResponseData>(
    `/api/modules?degreeId=${degreeId}&semesterId=${semesterId}`
  );
};

type UpdateModuleRequestBody = Omit<
  Module,
  "id" | "userId" | "degreeId" | "semesterId"
>;
const updateModuleRequest = async (
  body: UpdateModuleRequestBody,
  moduleId: string
) => {
  return await tuam.patch<void, UpdateModuleRequestBody>(
    `/api/modules/${moduleId}`,
    body
  );
};

export { newModuleRequest, getAllModules, updateModuleRequest };
