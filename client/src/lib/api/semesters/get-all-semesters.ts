import { tuam } from "@/lib/tuam";
import { Semester } from "@/types";

type AllSemestersResponseData = Semester[];

const getAllSemestersRequest = async (degreeId?: string) => {
  return await tuam.get<AllSemestersResponseData>(
    `/api/semesters?degreeId=${degreeId}`
  );
};

export { type AllSemestersResponseData, getAllSemestersRequest };
