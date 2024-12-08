import { tuam } from "@/lib/tuam";
import { Appointment } from "@/types";

type AppointmentsForCalendarResponseData = Appointment[];

// TODO: add optional from and to query parameters
const getAppointmentsForCalendarRequest = async (calendarId: string) => {
  const response = await tuam.get<
    {
      id: number;
      title: string;
      startDateTime: Date;
      endDateTime: Date;
      calendarId: number;
      description?: string;
    }[]
  >(`/api/calendars/${calendarId}/appointments`);

  const appointmentsForCalendarResponseData: AppointmentsForCalendarResponseData =
    response.map((m) => ({
      ...m,
      id: m.id.toString(),
      calendarId: m.calendarId.toString(),
    }));

  return appointmentsForCalendarResponseData;
};

export {
  type AppointmentsForCalendarResponseData,
  getAppointmentsForCalendarRequest,
};
