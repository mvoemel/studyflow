import { Module } from "./module";

export type Grade = {
  id: string;
  name: string;
  value: number;
  percentage: number;
  moduleId: Module["id"];
};

export type GradeViewTree = GradesViewSemester[];

export type GradesViewSemester = {
  semesterId: string;
  semesterName: string;
  modules: GradesViewModule[];
};

export type GradesViewModule = {
  moduleId: string;
  moduleName: string;
  moduleEcts: number;
  grades: Omit<Grade, "moduleId">[];
};
