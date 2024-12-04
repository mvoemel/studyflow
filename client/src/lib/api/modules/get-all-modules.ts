import { tuam } from "@/lib/tuam";
import { Module } from "@/types";

type AllModulesResponseData = Module[];

// TODO: add optional degreeId and semesterId query parameters
const getAllModulesRequest = async () => {
  const response = await tuam.get<
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
    }[]
  >(`/api/modules`);

  const allModulesResponseData: AllModulesResponseData = response.map((m) => ({
    ...m,
    id: m.id.toString(),
    semesterId: m.semesterId.toString(),
    degreeId: m.degreeId.toString(),
    userId: m.userId?.toString(),
  }));

  return allModulesResponseData;
};

export { type AllModulesResponseData, getAllModulesRequest };
