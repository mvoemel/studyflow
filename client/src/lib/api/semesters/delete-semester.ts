import { tuam } from "@/lib/tuam";

const deleteSemesterRequest = async (semesterId: string) => {
  return await tuam.delete<void>(`/api/semesters/${semesterId}`);
};

export { deleteSemesterRequest };
