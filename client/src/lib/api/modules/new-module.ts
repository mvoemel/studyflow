import { Module } from "@/types";
import { tuam } from "@/lib/tuam";

type NewModuleRequestBody = Omit<Module, "id" | "userId">;

const newModuleRequest = async (body: NewModuleRequestBody) => {
  return await tuam.post<Module, NewModuleRequestBody>("/api/modules", body);
};

export { type NewModuleRequestBody, newModuleRequest };
