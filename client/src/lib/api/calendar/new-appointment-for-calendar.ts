import { tuam } from "@/lib/tuam";
import { adjustToLocalTime } from "@/lib/utils";
import { Appointment } from "@/types";

type NewAppointmentForCalendarRequestBody = Omit<
  Appointment,
  "id" | "calendarId"
>;

const newAppointmentForCalendarRequest = async (
  calendarId: string,
  body: NewAppointmentForCalendarRequestBody
) => {
  const newBody: NewAppointmentForCalendarRequestBody = {
    ...body,
    startDateTime: adjustToLocalTime(body.startDateTime),
    endDateTime: adjustToLocalTime(body.endDateTime),
  };

  return await tuam.post<Appointment, NewAppointmentForCalendarRequestBody>(
    `/api/calendars/${calendarId}/appointments`,
    newBody
  );
};

export {
  type NewAppointmentForCalendarRequestBody,
  newAppointmentForCalendarRequest,
};
