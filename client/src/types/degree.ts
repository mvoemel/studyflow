import { Semester } from "./semester";
import { User } from "./user";

export type Degree = {
  id: string;
  name: string;
  activeSemesterId: Semester["id"];
  userId: User["id"];
  description?: string;
};
