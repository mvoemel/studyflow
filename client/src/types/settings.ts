import { Calendar } from "./calendar";
import { Degree } from "./degree";

export type Settings = {
  id: string;
  globalCalendarId: Calendar["id"];
  activeDegreeId?: Degree["id"];
};
