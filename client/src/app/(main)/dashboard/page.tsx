"use client";

import { ActiveModuleBox } from "@/components/dashboard/active-modules-box";
import { DailyCalendarBox } from "@/components/dashboard/daily-calendar-box";
import { EctsBox } from "@/components/dashboard/ects-box";
import { GradeBox } from "@/components/dashboard/grade-box";
import { ShortCutBox } from "@/components/dashboard/shortcut-box";

// TODO: fetch data from API and then render the dashboard components with fetched data
const DashboardPage = () => {
  return (
    <main className="grid flex-1 items-start py-4 md:p-4 gap-4 md:gap-8 lg:grid-cols-3">
      <DailyCalendarBox className="h-full" />

      <div className="grid auto-rows-max items-start gap-4 md:gap-8 lg:col-span-2">
        <div className="grid gap-4 grid-cols-6">
          <EctsBox
            className="col-span-3 md:col-span-2"
            ectsPoints={60}
            maxEctsPoints={90}
          />
          <GradeBox
            className="col-span-3 md:col-span-2"
            grade={4.6}
            description="Overall Grade"
          />
          <GradeBox
            className="col-span-3 md:col-span-2"
            grade={5.3}
            description="Current Semester Grade"
          />
          <ActiveModuleBox className="col-span-6" />
          <ShortCutBox
            className="col-span-6 md:col-span-3"
            title="New Module"
            buttonLabel="Create a module"
            buttonLink="#"
          />
          <ShortCutBox
            className="col-span-6 md:col-span-3"
            title="New Semester"
            description="Create a new semester for the currently selected Degree"
            buttonLabel="Create a semester"
            buttonLink="#"
          />
        </div>
      </div>
    </main>
  );
};

export default DashboardPage;
