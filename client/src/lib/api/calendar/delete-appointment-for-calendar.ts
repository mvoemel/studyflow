import { tuam } from "@/lib/tuam";

const deleteAppointmentForCalendarRequest = async (
  calendarId: string,
  appointmentId: string
) => {
  return await tuam.delete<void>(
    `/api/calendars/${calendarId}/appointments/${appointmentId}`
  );
};

export { deleteAppointmentForCalendarRequest };
