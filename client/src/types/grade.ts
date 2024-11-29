import { Module } from "./module";

export type Grade = {
  id: string;
  grade: number;
  percentage: number;
  moduleId: Module["id"];
};
