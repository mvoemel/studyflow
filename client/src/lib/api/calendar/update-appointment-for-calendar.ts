import { tuam } from "@/lib/tuam";
import { Appointment } from "@/types";

type UpdateAppointmentForCalendarRequestBody = Omit<
  Appointment,
  "id" | "calendarId"
>;

const updateAppointmentForCalendarRequest = async (
  calendarId: string,
  appointmentId: string,
  body: UpdateAppointmentForCalendarRequestBody
) => {
  return await tuam.post<void, UpdateAppointmentForCalendarRequestBody>(
    `/api/calendars/${calendarId}/appointments/${appointmentId}`,
    body
  );
};

export {
  type UpdateAppointmentForCalendarRequestBody,
  updateAppointmentForCalendarRequest,
};
