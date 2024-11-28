"use client";

import { useState } from "react";
import { ChevronDown } from "lucide-react";
import { semesterGradesMock } from "./mock-data";
import { Semester, Module } from "./types";
import clsx from "clsx";
import { Card } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";

// TODO: refactor this component and split into smaller components
// TODO: properly implement types

const GradesView = () => {
  return (
    <Card className="bg-muted/50 text-sm min-w-full max-w-5xl mx-auto p-4 rounded-lg">
      <div className="grid grid-cols-2 mb-2 px-4">
        <span>Semester</span>
        <span className="text-right">ECTS & Grade</span>
      </div>
      <div className="space-y-2">
        {semesterGradesMock.map((semester, index) => {
          // if is last return without separator
          if (index === semesterGradesMock.length - 1)
            return <SemesterSection key={index} semester={semester} />;

          return (
            <div key={index}>
              <SemesterSection semester={semester} />
              <Separator />
            </div>
          );
        })}
      </div>
    </Card>
  );
};

const SemesterSection = ({ semester }: { semester: Semester }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className="border-none">
      <button
        onClick={() => setIsExpanded(!isExpanded)}
        className="w-full bg-background p-4 rounded-md hover:bg-muted/50 flex flex-row items-center text-left"
        aria-expanded={isExpanded}
      >
        <ChevronDown
          className={`h-4 w-4 mr-2 shrink-0 transition-transform duration-200 ${
            isExpanded ? "rotate-180" : ""
          }`}
        />
        <div className="flex justify-between items-center w-full">
          <span>{semester.name}</span>
          <span className="flex items-center">
            <span className="mr-4 text-muted-foreground text-xs">
              {semester.ects}/{semester.ects}
            </span>
            <span
              className={clsx("", {
                "text-green-500": semester.grade >= 5,
                "text-yellow-500": semester.grade >= 4.5 && semester.grade < 5,
                "text-orange-500": semester.grade >= 4 && semester.grade < 4.5,
                "text-red-500": semester.grade < 4,
              })}
            >
              {semester.grade}
            </span>
          </span>
        </div>
      </button>
      <div
        className={`mt-2 space-y-2 pl-4 overflow-hidden transition-all duration-200 ${
          isExpanded ? "max-h-[1000px] opacity-100" : "max-h-0 opacity-0"
        }`}
      >
        {semester.modules.map((module, index) => (
          <ModuleSection key={index} module={module} />
        ))}
      </div>
    </div>
  );
};

const ModuleSection = ({ module }: { module: Module }) => {
  return (
    <div className="bg-opacity-50 p-4 rounded-md hover:bg-muted/50 flex justify-between items-center">
      <span>{module.name}</span>
      <span className="flex items-center">
        <span className="mr-4 text-muted-foreground text-xs">
          {module.ects}
        </span>
        <span>{module.grade}</span>
      </span>
    </div>
  );
};

export { GradesView };
