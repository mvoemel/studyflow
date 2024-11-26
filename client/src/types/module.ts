import { Degree } from "./degree";
import { Semester } from "./semester";
import { User } from "./user";

export type Module = {
  id: string;
  name: string;
  ects: number;
  complexity: number;
  understanding: number;
  time: number;
  semesterId: Semester["id"];
  degreeId: Degree["id"];
  userId: User["id"];
  description?: string;
};
