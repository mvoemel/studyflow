"use client";

import { useState } from "react";
import { ChevronDown } from "lucide-react";
import { degrees } from "./mock-data";
import { Degree, Semester, Module } from "./types";
import clsx from "clsx";
import { Card } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";

// TODO: reafactor this component and split into smaller components

const GradesView = () => {
  return (
    <Card className="bg-muted/50 text-sm min-w-full max-w-5xl mx-auto p-4 rounded-lg">
      <div className="grid grid-cols-2 mb-2 px-4">
        <span>Name</span>
        <span className="text-right">ECTS & Grade</span>
      </div>
      <div className="space-y-2">
        {degrees.map((degree, index) => {
          // if is last return without separator
          if (index === degrees.length - 1)
            return <DegreeSection key={index} degree={degree} />;

          return (
            <div key={index}>
              <DegreeSection degree={degree} />
              <Separator />
            </div>
          );
        })}
      </div>
    </Card>
  );
};

const DegreeSection = ({ degree }: { degree: Degree }) => {
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
          <span>{degree.name}</span>
          <span className="flex items-center">
            <span className="mr-4 text-muted-foreground text-xs">
              {degree.totalEcts}/{degree.totalEcts}
            </span>
            <span
              className={clsx("", {
                "text-green-500": degree.totalGrade >= 5,
                "text-yellow-500":
                  degree.totalGrade >= 4.5 && degree.totalGrade < 5,
                "text-orange-500":
                  degree.totalGrade >= 4 && degree.totalGrade < 4.5,
                "text-red-500": degree.totalGrade < 4,
              })}
            >
              {degree.totalGrade}
            </span>
          </span>
        </div>
      </button>
      <div
        className={`mt-2 space-y-2 pl-4 overflow-hidden transition-all duration-200 ${
          isExpanded ? "max-h-[1000px] opacity-100" : "max-h-0 opacity-0"
        }`}
      >
        {degree.semesters.map((semester, index) => (
          <SemesterSection key={index} semester={semester} />
        ))}
      </div>
    </div>
  );
};

const SemesterSection = ({ semester }: { semester: Semester }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div className="border-none">
      <button
        onClick={() => setIsExpanded(!isExpanded)}
        className="w-full p-4 rounded-md hover:bg-muted/50 flex flex-row items-center text-left"
        aria-expanded={isExpanded}
      >
        <ChevronDown
          className={`h-4 w-4 mr-2 shrink-0 transition-transform duration-2000 ${
            isExpanded ? "rotate-180" : ""
          }`}
        />
        <div className="flex justify-between items-center w-full">
          <span>{semester.name}</span>
          <span className="flex items-center">
            <span className="mr-4 text-muted-foreground text-xs">
              {semester.ects}
            </span>
            <span>{semester.grade}</span>
          </span>
        </div>
      </button>
      <div
        className={`mt-1 space-y-1 pl-4 overflow-hidden transition-all duration-200 ${
          isExpanded ? "max-h-[1000px] opacity-100" : "max-h-0 opacity-0"
        }`}
      >
        {semester.modules.map((module, index) => (
          <ModuleRow key={index} module={module} />
        ))}
      </div>
    </div>
  );
};

const ModuleRow = ({ module }: { module: Module }) => {
  return (
    <div className="border-[1.5px] bg-opacity-50 p-4 rounded-md hover:bg-muted/50 flex justify-between items-center">
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
