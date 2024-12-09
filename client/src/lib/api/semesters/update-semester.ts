import { tuam } from "@/lib/tuam";
import { Semester } from "@/types";

type UpdateSemesterRequestBody = Pick<Semester, "name" | "description">;

const updateSemesterRequest = async (
  body: UpdateSemesterRequestBody,
  semesterId: string
) => {
  return await tuam.post<void, UpdateSemesterRequestBody>(
    `/api/semesters/${semesterId}`,
    body
  );
};

export { type UpdateSemesterRequestBody, updateSemesterRequest };
