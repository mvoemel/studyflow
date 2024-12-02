import { tuam } from "@/lib/tuam";
import { Semester } from "@/types";

type AllSemestersResponseData = Semester[];

// TODO: add optional degreeId query parameters
const getAllSemestersRequest = async () => {
  return await tuam.get<AllSemestersResponseData>(`/api/semesters`);
};

export { type AllSemestersResponseData, getAllSemestersRequest };
