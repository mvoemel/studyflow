"use client";

import { ActiveModuleBox } from "@/components/dashboard/active-modules-box";
import { DailyCalendarBox } from "@/components/dashboard/daily-calendar-box";
import { EctsBox } from "@/components/dashboard/ects-box";
import { GradeBox } from "@/components/dashboard/grade-box";
import { ShortCutBox } from "@/components/dashboard/shortcut-box";
import { useEctsAverage } from "@/hooks/use-ects-average";
import { useUserSettings } from "@/hooks/use-user-settings";

const DashboardPage = () => {
  const { settings } = useUserSettings();
  const { average, currEcts, totalEcts } = useEctsAverage(
    settings?.activeDegreeId
  );

  return (
    <main className="grid grid-cols-1 md:grid-cols-3 auto-rows-max md:grid-rows-3 gap-4 h-full p-4">
      <DailyCalendarBox className="h-full md:row-span-3" />
      <EctsBox
        className="col-span-1"
        ectsPoints={currEcts}
        maxEctsPoints={totalEcts}
      />
      <GradeBox
        className="col-span-1"
        grade={average}
        description="Average Grade"
      />
      <ActiveModuleBox className="col-span-1 md:col-span-2" />
      <ShortCutBox
        className="col-span-1"
        title="Schedule"
        description="Watch your detailed schedule plan for your current semester!"
        buttonLabel="My Schedule"
        buttonLink="/schedule"
      />
      <ShortCutBox
        className="col-span-1"
        title="Grades"
        description="Check out your grades for your current Degree!"
        buttonLabel="My Grades"
        buttonLink="/grades"
      />
    </main>
  );
};

export default DashboardPage;
