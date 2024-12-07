import { tuam } from "@/lib/tuam";
import { Semester } from "@/types";

type AllSemestersResponseData = Semester[];

// TODO: add optional degreeId query parameters
const getAllSemestersRequest = async () => {
  const response = await tuam.get<
    {
      id: number;
      name: string;
      degreeId: number;
      userId: number;
      calendarId?: number;
      description?: string;
    }[]
  >("/api/semesters");

  const allSemestersResponseData: AllSemestersResponseData = response.map(
    (s) => ({
      ...s,
      id: s.id.toString(),
      degreeId: s.degreeId.toString(),
      userId: s.userId.toString(),
      calendarId: s.calendarId === -1 ? undefined : s.calendarId?.toString(),
    })
  );

  return allSemestersResponseData;
};

export { type AllSemestersResponseData, getAllSemestersRequest };
