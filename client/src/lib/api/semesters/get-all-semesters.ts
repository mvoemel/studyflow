import { tuam } from "@/lib/tuam";
import { Semester } from "@/types";

type AllSemestersResponseData = Semester[];

// TODO: add optional degreeId query parameters
// TODO: fix type
const getAllSemestersRequest = async () => {
  const response = await tuam.get<AllSemestersResponseData>("/api/semesters");
  return response.map((s) => ({
    ...s,
    id: s.id.toString(),
    degreeId: s.degreeId.toString(),
    userId: s.userId.toString(),
    calendarId: s.calendarId?.toString(),
  }));
};

export { type AllSemestersResponseData, getAllSemestersRequest };
