import { Module } from "@/types";
import { tuam } from "@/lib/tuam";

type NewModuleRequestBody = Omit<Module, "id" | "userId">;

// TODO: fix return type value (.post<Module, ...)
const newModuleRequest = async (body: NewModuleRequestBody) => {
  const response = await tuam.post<Module, NewModuleRequestBody>(
    "/api/modules",
    body
  );
  return {
    ...response,
    id: response.id.toString(),
    semesterId: response.semesterId.toString(),
    degreeId: response.degreeId.toString(),
    userId: response.userId.toString(),
  };
};

export { type NewModuleRequestBody, newModuleRequest };
