import { Calendar } from "./calendar";
import { Degree } from "./degree";
import { Semester } from "./semester";

export type Settings = {
  id: string;
  globalCalendarId: Calendar["id"];
  activeDegreeId: Degree["id"];
  activeSemesterId: Semester["id"];
};
