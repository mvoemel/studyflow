import { Module } from "@/types";
import { tuam } from "@/lib/tuam";

type NewModuleRequestBody = Omit<Module, "id" | "userId">;

const newModuleRequest = async (body: NewModuleRequestBody) => {
  const response = await tuam.post<
    {
      id: number;
      name: string;
      ects: number;
      complexity: number;
      understanding: number;
      time: number;
      semesterId: number;
      degreeId: number;
      userId: number;
      description?: string;
    },
    NewModuleRequestBody
  >("/api/modules", body);

  const newModuleResponseData: Module = {
    ...response,
    id: response.id.toString(),
    semesterId: response.semesterId.toString(),
    degreeId: response.degreeId.toString(),
    userId: response.userId?.toString(),
  };

  return newModuleResponseData;
};

export { type NewModuleRequestBody, newModuleRequest };
