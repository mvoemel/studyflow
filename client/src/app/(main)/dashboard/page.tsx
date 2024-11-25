"use client";

import { ActiveModuleBox } from "@/components/dashboard/active-modules-box";
import { DailyCalendarBox } from "@/components/dashboard/daily-calendar-box";
import { EctsBox } from "@/components/dashboard/ects-box";
import { GradeBox } from "@/components/dashboard/grade-box";
import { ShortCutBox } from "@/components/dashboard/shortcut-box";

// TODO: fetch data from API and then render the dashboard components with fetched data
const DashboardPage = () => {
  return (
    <main className="grid grid-cols-1 md:grid-cols-3 auto-rows-max md:grid-rows-3 gap-4 h-full p-4">
      <DailyCalendarBox className="h-full md:row-span-3" />
      <EctsBox className="col-span-1" ectsPoints={60} maxEctsPoints={90} />
      <GradeBox
        className="col-span-1"
        grade={4.6}
        description="Overall Grade"
      />
      <ActiveModuleBox className="col-span-1 md:col-span-2" />
      <ShortCutBox
        className="col-span-1"
        title="New Semester"
        description="Create a new semester for the currently selected Degree"
        buttonLabel="Create a semester"
        buttonLink="#"
      />
      <ShortCutBox
        className="col-span-1"
        title="New Semester"
        description="Create a new semester for the currently selected Degree"
        buttonLabel="Create a semester"
        buttonLink="#"
      />
    </main>
  );
};

export default DashboardPage;
