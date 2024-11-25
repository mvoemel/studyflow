import { Semester } from "./semester";
import { User } from "./user";

export type Module = {
  id: string;
  name: string;
  description: string;
  ects: number;
  complexity: number;
  understanding: number;
  time: number;
  semesterId: Semester["id"];
  userId: User["id"];
};
