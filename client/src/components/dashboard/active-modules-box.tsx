import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useDegrees } from "@/hooks/use-degree";
import { useModules } from "@/hooks/use-modules";
import { useSemesters } from "@/hooks/use-semester";
import { useUserSettings } from "@/hooks/use-user-settings";
import { cn } from "@/lib/utils";
import { useMemo } from "react";
import { Skeleton } from "../ui/skeleton";
import { Separator } from "../ui/separator";

type ActiveModuleBoxProps = {
  className?: string;
};

const ActiveModuleBox = ({ className }: ActiveModuleBoxProps) => {
  const { settings } = useUserSettings();
  const { degrees } = useDegrees();
  const { semesters } = useSemesters();
  const { modules } = useModules();

  const filteredModules = useMemo(() => {
    if (!degrees || !semesters || !modules) return undefined;

    const currDegree = degrees?.find((d) => d.id === settings?.activeDegreeId);

    if (!currDegree || !currDegree.activeSemesterId) {
      return undefined;
    }

    return modules.filter((m) => m.semesterId === currDegree.activeSemesterId);
  }, [degrees, semesters, modules, settings?.activeDegreeId]);

  if (!degrees || !semesters || !modules)
    return <ActiveModuleBoxSkeleton className={className} />;

  return (
    <Card className={cn(className, "bg-muted/50")}>
      <CardHeader className="pb-3">
        <CardTitle>Active Modules</CardTitle>
      </CardHeader>
      <CardContent className="text-balance leading-relaxed overflow-scroll">
        {filteredModules?.map((m) => (
          <>
            <div
              key={m.id}
              className="w-full flex justify-between items-center gap-2 pr-2 text-muted-foreground"
            >
              <div>
                <p className="text-foreground">{m.name}</p>
                <p className="text-xs">{m.description}</p>
              </div>
              <p className="flex items-center gap-2 text-foreground">
                {m.ects} <p className="text-xs text-muted-foreground">ects</p>
              </p>
            </div>
            <Separator />
          </>
        ))}
      </CardContent>
    </Card>
  );
};

const ActiveModuleBoxSkeleton = ({ className }: ActiveModuleBoxProps) => {
  return (
    <Card className={cn(className, "flex flex-col gap-4 bg-muted/50 p-4")}>
      <Skeleton className="h-6 w-[150px]" />
      <div className="space-y-2">
        <Skeleton className="h-4 w-[500px]" />
        <Skeleton className="h-2 w-[300px]" />
      </div>
      <div className="space-y-2">
        <Skeleton className="h-4 w-[400px]" />
        <Skeleton className="h-2 w-[200px]" />
      </div>
      <div className="space-y-2">
        <Skeleton className="h-4 w-[450px]" />
        <Skeleton className="h-2 w-[250px]" />
      </div>
    </Card>
  );
};

export { ActiveModuleBox };
