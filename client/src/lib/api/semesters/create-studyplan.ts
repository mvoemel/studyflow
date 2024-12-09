import { tuam } from "@/lib/tuam";

const DAYS_OF_WEEK = [
  { id: "SUNDAY", name: "Sunday" },
  { id: "MONDAY", name: "Monday" },
  { id: "TUESDAY", name: "Tuesday" },
  { id: "WEDNESDAY", name: "Wednesday" },
  { id: "THURSDAY", name: "Thursday" },
  { id: "FRIDAY", name: "Friday" },
  { id: "SATURDAY", name: "Saturday" },
] as const;

type DayId = (typeof DAYS_OF_WEEK)[number]["id"];

type CreateStudyplanRequestBody = {
  settingsId: string;
  semesterId: string;
  startDate: string;
  endDate: string;
  daysOfWeek: DayId[];
  dayStartTime: string;
  dayEndTime: string;
};

type CreateStudyplanResponseData = { calendarId: string };

const createStudyplanRequest = async (body: CreateStudyplanRequestBody) => {
  return await tuam.post<
    CreateStudyplanResponseData,
    CreateStudyplanRequestBody
  >("/api/studyplan", body);
};

export {
  DAYS_OF_WEEK,
  type DayId,
  type CreateStudyplanRequestBody,
  type CreateStudyplanResponseData,
  createStudyplanRequest,
};
