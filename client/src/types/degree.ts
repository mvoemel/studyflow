// import { SemesterWithModules } from "./semester";
import { User } from "./user";

export type Degree = {
  id: string;
  name: string;
  description: string;
  userId: User["id"];
};

// export type DegreeWithSemester = Omit<Degree, "userId"> & {
//   semesters: SemesterWithModules[];
// };
