import {
  AllModulesResponseData,
  deleteModuleRequest,
  getAllModulesRequest,
  newModuleRequest,
  NewModuleRequestBody,
  updateModuleRequest,
  UpdateModuleRequestBody,
} from "@/lib/api";
import { Module } from "@/types";
import useSWR from "swr";

const useModules = () => {
  const { data, error, mutate, isLoading } = useSWR<AllModulesResponseData>(
    "modules",
    () => getAllModulesRequest()
  );

  const addNewModule = async (body: NewModuleRequestBody) => {
    const optimisticModule = {
      ...body,
      id: `tmp-module-id-${Date.now()}`,
      userId: `tmp-user-id-${Date.now()}`,
    };

    await mutate(
      async () => {
        const addedModule = await newModuleRequest(body);
        return [...(data || []), addedModule];
      },
      {
        optimisticData: [...(data || []), optimisticModule],
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const updateModule = async (
    body: UpdateModuleRequestBody,
    moduleId: Module["id"]
  ) => {
    await mutate(
      async () => {
        await updateModuleRequest(body, moduleId);
        return (data || []).map((d) =>
          d.id === moduleId ? { ...d, ...body } : d
        );
      },
      {
        optimisticData: (data || []).map((d) =>
          d.id === moduleId ? { ...d, ...body } : d
        ),
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const deleteModule = async (moduleId: Module["id"]) => {
    await mutate(
      async () => {
        await deleteModuleRequest(moduleId);
        return (data || []).filter((d) => d.id !== moduleId);
      },
      {
        optimisticData: (data || []).filter((d) => d.id !== moduleId),
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  return {
    modules: data,
    isLoading,
    error,
    addNewModule,
    updateModule,
    deleteModule,
  };
};

export { useModules };
