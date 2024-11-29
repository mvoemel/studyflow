import {
  AllModulesResponseData,
  getAllModulesRequest,
  newModuleRequest,
  NewModuleRequestBody,
  updateModuleRequest,
  UpdateModuleRequestBody,
} from "@/lib/api";
import { Module } from "@/types";
import useSWR from "swr";

const useModules = () => {
  // BUG: "modules" is passed to the getAllModulesRequest method
  const { data, error, mutate, isLoading } = useSWR<AllModulesResponseData>(
    "modules",
    getAllModulesRequest
  );

  const addNewModule = async (body: NewModuleRequestBody) => {
    await newModuleRequest(body);
    mutate(); // TODO: change so new request is not sent
  };

  const updateModule = async (
    body: UpdateModuleRequestBody,
    moduleId: Module["id"]
  ) => {
    await updateModuleRequest(body, moduleId);
    mutate(); // TODO: change so new request is not sent
  };

  //   const deleteModule = async (moduleId: Module["id"]) => {
  //     await deleteModuleRequest(moduleId);
  //     mutate(); // TODO: change so new request is not sent
  //   };

  return {
    modules: data,
    isLoading,
    error,
    addNewModule,
    updateModule,
    // deleteModule,
  };
};

export { useModules };
