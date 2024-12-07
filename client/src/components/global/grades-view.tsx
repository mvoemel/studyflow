"use client";

import { useMemo, useState } from "react";
import { ChevronDown, PenIcon, CirclePlus } from "lucide-react";
import clsx from "clsx";
import { Card } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { GradesViewModule, GradesViewSemester, GradeViewTree } from "@/types";
import { Button } from "@/components/ui/button";
import { AddGradeDialog } from "@/components/dialogs/add-grade";

type GradesViewProps = {
  gradesTree: GradeViewTree;
};

const GradesView = ({ gradesTree }: GradesViewProps) => {
  return (
    <Card className="bg-muted/50 text-sm min-w-full max-w-5xl mx-auto p-4 rounded-lg">
      <div className="grid grid-cols-2 mb-2 px-4">
        <span>Semester</span>
        <span className="text-right">ECTS & Grade</span>
      </div>
      <div className="space-y-2">
        {gradesTree.map((semester, index) => {
          // if is last return without separator
          if (index === gradesTree.length - 1)
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

const SemesterSection = ({ semester }: { semester: GradesViewSemester }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const ects = useMemo(
    () =>
      semester.modules.reduce(
        (prevVal, currModule) => prevVal + currModule.moduleEcts,
        0
      ),
    [semester]
  );

  const grade = useMemo(() => {
    const moduleGrades = semester.modules.map((m) => {
      return {
        grade: m.grades.reduce(
          (prevVal, currGrade) =>
            prevVal + currGrade.percentage * currGrade.value,
          0
        ),
        ects: m.moduleEcts,
      };
    });

    const totalEcts = moduleGrades.reduce(
      (prevVal, currGrade) => prevVal + currGrade.ects,
      0
    );

    return moduleGrades.reduce(
      (prevVal, currModule) =>
        prevVal + (currModule.grade * currModule.ects) / totalEcts,
      0
    );
  }, [semester]);

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
          <span>{semester.semesterName}</span>
          <span className="flex items-center">
            <span className="mr-4 text-muted-foreground text-xs">{ects}</span>
            <span
              className={clsx("", {
                "text-green-500": grade >= 5,
                "text-yellow-500": grade >= 4.5 && grade < 5,
                "text-orange-500": grade >= 4 && grade < 4.5,
                "text-red-500": grade >= 1 && grade < 4,
              })}
            >
              {grade === 0 ? "-" : grade.toFixed(2)}
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

const ModuleSection = ({ module }: { module: GradesViewModule }) => {
  const [isGradeDialogOpen, setIsGradeDialogOpen] = useState(false);

  const grade = useMemo(
    () =>
      module.grades.reduce(
        (previousValue, currentGrade) =>
          previousValue + currentGrade.percentage * currentGrade.value,
        0
      ),
    [module]
  );

  return (
    <div className="bg-opacity-50 p-4 rounded-md hover:bg-muted/50 flex justify-between items-center">
      <span>{module.moduleName}</span>
      <span className="flex items-center">
        <span className="mr-4 text-muted-foreground text-xs">
          {module.moduleEcts}
        </span>
        <span>{grade === 0 ? "-" : grade.toFixed(2)}</span>
        <Button
          className="p-0"
          variant="ghost"
          size="icon"
          onClick={() => setIsGradeDialogOpen(true)}
        >
          <CirclePlus className="h-4" />
        </Button>

        <AddGradeDialog
          isOpen={isGradeDialogOpen}
          onClose={() => setIsGradeDialogOpen(false)}
          grades={module.grades}
        />
      </span>
    </div>
  );
};

export { GradesView };
