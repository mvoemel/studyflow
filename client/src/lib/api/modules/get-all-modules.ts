import { tuam } from "@/lib/tuam";
import { Module } from "@/types";

type AllModulesResponseData = Module[];

// TODO: add optional degreeId and semesterId query parameters
const getAllModulesRequest = async () => {
  return await tuam.get<AllModulesResponseData>(`/api/modules`);
};

export { type AllModulesResponseData, getAllModulesRequest };
