import { tuam } from "@/lib/tuam";
import { Module } from "@/types";

type AllModulesResponseData = Module[];

const getAllModulesRequest = async (degreeId?: string, semesterId?: string) => {
  return await tuam.get<AllModulesResponseData>(
    `/api/modules?degreeId=${degreeId}&semesterId=${semesterId}`
  );
};

export { type AllModulesResponseData, getAllModulesRequest };
