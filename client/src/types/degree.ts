import { Semester } from "./semester";
import { User } from "./user";

export type Degree = {
  id: string;
  name: string;
  userId: User["id"];
  activeSemesterId?: Semester["id"];
  description?: string;
};
