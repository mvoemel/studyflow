import { Calendar } from "./calendar";

export type Appointment = {
  id: string;
  title: string;
  description: string;
  startDateTime: Date;
  endDateTime: Date;
  calendarId: Calendar["id"];
};
