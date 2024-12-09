import { tuam } from "@/lib/tuam";
import { adjustToLocalTime } from "@/lib/utils";
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
  const newBody: UpdateAppointmentForCalendarRequestBody = {
    ...body,
    startDateTime: adjustToLocalTime(body.startDateTime),
    endDateTime: adjustToLocalTime(body.endDateTime),
  };

  return await tuam.post<void, UpdateAppointmentForCalendarRequestBody>(
    `/api/calendars/${calendarId}/appointments/${appointmentId}`,
    newBody
  );
};

export {
  type UpdateAppointmentForCalendarRequestBody,
  updateAppointmentForCalendarRequest,
};
