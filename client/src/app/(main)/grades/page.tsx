"use client";

import { GradesView } from "@/components/global/grades-view";
import { Card } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { useGrades } from "@/hooks/use-grades";
import { useUserSettings } from "@/hooks/use-user-settings";

const GradesPage = () => {
  const { settings } = useUserSettings();
  const { gradesTree } = useGrades(settings?.activeDegreeId);

  if (!gradesTree) return <GradesPageSkeleton />;

  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <GradesView gradesTree={gradesTree} />
    </div>
  );
};

const GradesPageSkeleton = () => {
  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <Card className="bg-muted/50 text-sm min-w-full max-w-5xl mx-auto p-4 rounded-lg space-y-2">
        <div className="flex justify-between">
          <Skeleton className="ml-4 h-5 w-20" />
          <Skeleton className="h-5 w-32" />
        </div>
        <Skeleton className="h-[200px] w-full" />
      </Card>
    </div>
  );
};

export default GradesPage;
