import { Calendar } from "./calendar";
import { Degree } from "./degree";
import { User } from "./user";

export type Semester = {
  id: string;
  name: string;
  degreeId: Degree["id"];
  userId: User["id"];
  calendarId?: Calendar["id"];
  description?: string;
};
