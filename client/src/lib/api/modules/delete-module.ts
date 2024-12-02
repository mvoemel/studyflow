import { tuam } from "@/lib/tuam";

const deleteModuleRequest = async (moduleId: string) => {
  return await tuam.delete<void>(`/api/modules/${moduleId}`);
};

export { deleteModuleRequest };
