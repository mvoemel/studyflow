import { tuam } from "@/lib/tuam";
import { Calendar } from "@/types";

type CalendarResponseData = Calendar;

const getCalendarRequest = async (calendarId: string) => {
  return await tuam.get<CalendarResponseData>(`/api/calendars/${calendarId}`);
};

export { type CalendarResponseData, getCalendarRequest };
