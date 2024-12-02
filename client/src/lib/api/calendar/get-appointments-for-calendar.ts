import { tuam } from "@/lib/tuam";
import { Appointment } from "@/types";

type AppointmentsForCalendarResponseData = Appointment[];

// TODO: add optional from and to query parameters
const getAppointmentsForCalendarRequest = async (calendarId: string) => {
  return await tuam.get<AppointmentsForCalendarResponseData>(
    `/api/calendars/${calendarId}/appointments`
  );
};

export {
  type AppointmentsForCalendarResponseData,
  getAppointmentsForCalendarRequest,
};
