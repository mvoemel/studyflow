import { tuam } from "@/lib/tuam";

type CreateStudyplanRequestBody = {
  startDate: Date;
  endDate: Date;
  daysOfWeek: [boolean, boolean, boolean, boolean, boolean, boolean, boolean];
  dayStartTime: Date;
  dayEndTime: Date;
};

type CreateStudyplanResponseData = { calendarId: string };

const createStudyplanRequest = async (
  body: CreateStudyplanRequestBody,
  semesterId: string
) => {
  return await tuam.post<
    CreateStudyplanResponseData,
    CreateStudyplanRequestBody
  >(`/api/semesters/${semesterId}/studyplan`, body);
};

export {
  type CreateStudyplanRequestBody,
  type CreateStudyplanResponseData,
  createStudyplanRequest,
};
