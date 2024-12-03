import { tuam } from "@/lib/tuam";
import { Semester } from "@/types";

type NewSemesterRequestBody = Pick<
  Semester,
  "name" | "description" | "degreeId"
>;

// TODO: fix type
const newSemesterRequest = async (body: NewSemesterRequestBody) => {
  const response = await tuam.post<Semester, NewSemesterRequestBody>(
    "/api/semesters",
    body
  );
  return {
    ...response,
    id: response.id.toString(),
    degreeId: response.degreeId.toString(),
    userId: response.userId.toString(),
    calendarId: response.calendarId?.toString(),
  };
};

export { type NewSemesterRequestBody, newSemesterRequest };
