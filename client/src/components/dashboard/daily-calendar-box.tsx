"use client";

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { Skeleton } from "../ui/skeleton";
import { EventSourceInput } from "@fullcalendar/core/index.js";
import { useMemo } from "react";
import { useAppointments } from "@/hooks/use-appointments";
import { useUserSettings } from "@/hooks/use-user-settings";
import { useDegrees } from "@/hooks/use-degree";
import { useSemesters } from "@/hooks/use-semester";

type DailyCalendarBoxProps = {
  className?: string;
};

const DailyCalendarBox = ({ className }: DailyCalendarBoxProps) => {
  const { settings } = useUserSettings();
  const { degrees } = useDegrees();
  const { semesters } = useSemesters();

  const currSemester = useMemo(() => {
    if (!degrees || !semesters || !settings?.activeDegreeId) return undefined;

    const currDegree = degrees?.find((d) => d.id === settings.activeDegreeId);

    if (!currDegree || !currDegree.activeSemesterId) return undefined;

    const currSemester = semesters?.find(
      (s) => s.id === currDegree.activeSemesterId
    );

    return currSemester;
  }, [settings, degrees, semesters]);

  const { appointments: globalAppointments } = useAppointments(
    settings?.globalCalendarId
  );
  const { appointments: currSemesterAppointments } = useAppointments(
    currSemester?.calendarId
  );

  const events = useMemo(() => {
    const globalEvents: EventSourceInput =
      globalAppointments?.map((a) => ({
        ...a,
        start: a.startDateTime,
        end: a.endDateTime,
      })) || [];
    const currSemesterEvents: EventSourceInput =
      currSemesterAppointments?.map((a) => ({
        ...a,
        start: a.startDateTime,
        end: a.endDateTime,
      })) || [];

    return globalEvents.concat(currSemesterEvents);
  }, [globalAppointments, currSemesterAppointments]);

  if (!events) return <DailyCalendarBoxSkeleton className={className} />;

  return (
    <Card className={cn(className, "bg-muted/50")}>
      <CardHeader className="pb-3">
        <CardDescription>Schedule for Today</CardDescription>
      </CardHeader>
      <CardContent className="h-full">
        <FullCalendar
          plugins={[timeGridPlugin, interactionPlugin]}
          initialView="timeGridDay"
          weekends={true}
          nowIndicator={true}
          allDaySlot={false}
          editable={false}
          events={events}
          slotMinTime={"07:00:00"}
          slotMaxTime={"23:00:00"}
          slotLabelFormat={{
            hour: "2-digit",
            minute: "2-digit",
            hour12: false,
          }}
          eventTimeFormat={{
            hour: "2-digit",
            minute: "2-digit",
            hour12: false,
          }}
          titleFormat={{
            month: "short",
            year: "numeric",
            day: "2-digit",
          }}
          height="90%"
        />
      </CardContent>
    </Card>
  );
};

const DailyCalendarBoxSkeleton = ({
  className,
}: Pick<DailyCalendarBoxProps, "className">) => {
  return (
    <Card className={cn(className, "flex flex-col bg-muted/50 gap-4 p-4")}>
      <div className="space-y-2">
        <Skeleton className="h-3 w-[180px]" />
        <Skeleton className="h-12 w-[250px] rounded-md" />
      </div>
      <Skeleton className="h-full w-full" />
    </Card>
  );
};

export { DailyCalendarBox };
