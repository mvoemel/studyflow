import { Semester } from "@/types";
import { tuam } from "../tuam";

type NewSemesterRequestBody = Pick<
  Semester,
  "name" | "description" | "degreeId"
>;
const newSemesterRequest = async (body: NewSemesterRequestBody) => {
  return await tuam.post<void, NewSemesterRequestBody>("/api/semesters", body);
};

type AllSemestersResponseData = Semester[];
const getAllSemesters = async (degreeId?: string) => {
  return await tuam.get<AllSemestersResponseData>(
    `/api/semesters?degreeId=${degreeId}`
  );
};

type UpdateSemesterRequestBody = Pick<Semester, "name" | "description">;
const updateSemesterRequest = async (
  body: UpdateSemesterRequestBody,
  semesterId: string
) => {
  return await tuam.patch<void, UpdateSemesterRequestBody>(
    `/api/semesters/${semesterId}`,
    body
  );
};

export { newSemesterRequest, getAllSemesters, updateSemesterRequest };
