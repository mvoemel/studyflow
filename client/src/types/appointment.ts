import { Calendar } from "./calendar";
import { User } from "./user";

export type Appointment = {
  id: string;
  title: string;
  startDateTime: Date;
  endDateTime: Date;
  calendarId: Calendar["id"];
  userId: User["id"];
  description?: string;
};
