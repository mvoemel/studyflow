import { Degree } from "./degree";
import { User } from "./user";
// import { Module } from "./module";

export type Semester = {
  id: string;
  name: string;
  description: string;
  degreeId: Degree["id"];
  userId: User["id"];
};

// export type SemesterWithModules = Omit<Semester, "degreeId"> & {
//   modules: Module[];
// };
