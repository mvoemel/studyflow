import { tuam } from "@/lib/tuam";

const deleteDegreeRequest = async (degreeId: string) => {
  return await tuam.delete<void>(`/api/degrees/${degreeId}`);
};

export { deleteDegreeRequest };
