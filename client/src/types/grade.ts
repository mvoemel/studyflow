import { Module } from "./module";

export type Grade = {
  id: string;
  name: string;
  value: number;
  percentage: number;
  moduleId: Module["id"];
};

export type GradeViewTree = {
  semesterId: string;
  semesterName: string;
  modules: {
    moduleId: string;
    moduleName: string;
    grades: Omit<Grade, "moduleId">[];
  }[];
}[];
