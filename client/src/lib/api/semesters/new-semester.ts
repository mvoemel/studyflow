import { tuam } from "@/lib/tuam";
import { Semester } from "@/types";

type NewSemesterRequestBody = Pick<
  Semester,
  "name" | "description" | "degreeId"
>;

const newSemesterRequest = async (body: NewSemesterRequestBody) => {
  const response = await tuam.post<
    {
      id: number;
      name: string;
      degreeId: number;
      userId: number;
      calendarId?: number;
      description?: string;
    },
    NewSemesterRequestBody
  >("/api/semesters", body);

  const calendarId =
    response.calendarId === -1 ? undefined : response.calendarId;

  const newSemesterResponseData: Semester = {
    ...response,
    id: response.id.toString(),
    degreeId: response.degreeId.toString(),
    userId: response.userId?.toString(),
    calendarId: calendarId?.toString(),
  };

  return newSemesterResponseData;
};

export { type NewSemesterRequestBody, newSemesterRequest };
