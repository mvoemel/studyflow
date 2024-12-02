import { tuam } from "@/lib/tuam";
import { Semester } from "@/types";

type NewSemesterRequestBody = Pick<
  Semester,
  "name" | "description" | "degreeId"
>;

const newSemesterRequest = async (body: NewSemesterRequestBody) => {
  return await tuam.post<Semester, NewSemesterRequestBody>(
    "/api/semesters",
    body
  );
};

export { type NewSemesterRequestBody, newSemesterRequest };
