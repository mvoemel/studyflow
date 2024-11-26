import { Calendar } from "./calendar";

export type Appointment = {
  id: string;
  title: string;
  startDateTime: Date;
  endDateTime: Date;
  calendarId: Calendar["id"];
  description?: string;
};
