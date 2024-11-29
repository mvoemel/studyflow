import { tuam } from "@/lib/tuam";
import { Appointment } from "@/types";

type NewAppointmentForCalendarRequestBody = Omit<
  Appointment,
  "id" | "calendarId"
>;

const newAppointmentForCalendarRequest = async (
  calendarId: string,
  body: NewAppointmentForCalendarRequestBody
) => {
  return await tuam.post<void, NewAppointmentForCalendarRequestBody>(
    `/api/calendars/${calendarId}/appointments`,
    body
  );
};

export {
  type NewAppointmentForCalendarRequestBody,
  newAppointmentForCalendarRequest,
};
