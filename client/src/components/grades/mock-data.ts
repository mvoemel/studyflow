import { Semester } from "@/types";
import { Degree } from "./types";

const semesterGradesMock: Semester[] = [
  {
    name: "1. Semester",
    ects: 30,
    grade: 5.5,
    modules: [
      { name: "Programming 1", ects: 15, grade: 5.6 },
      { name: "Linear Algebra", ects: 15, grade: 5.4 },
    ],
  },
  {
    name: "2. Semester",
    ects: 30,
    grade: 5.1,
    modules: [
      { name: "Programming 2", ects: 15, grade: 5 },
      {
        name: "System Design and Computer Architecture",
        ects: 15,
        grade: 5.2,
      },
    ],
  },
  {
    name: "3. Semester",
    ects: 30,
    grade: "-",
    modules: [
      { name: "Algorithms and Datastructures", ects: 15, grade: "-" },
      { name: "Software Development 1", ects: 15, grade: "-" },
    ],
  },
];

export { semesterGradesMock };
