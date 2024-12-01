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
import { useEvents } from "@/hooks/use-events";

type DailyCalendarBoxProps = {
  className?: string;
};

const DailyCalendarBox = ({ className }: DailyCalendarBoxProps) => {
  const { events } = useEvents();

  if (!events) return <DailyCalendarBoxSkeleton className={className} />;

  return (
    <Card className={cn(className, "bg-muted/50")}>
      <CardHeader className="pb-3">
        <CardDescription>Schedule for Today</CardDescription>
      </CardHeader>
      <CardContent className="h-full">
        <FullCalendar
          plugins={[timeGridPlugin, interactionPlugin]}
          initialView={"timeGridDay"}
          weekends={true}
          nowIndicator={true}
          editable={false}
          events={events}
          slotMinTime={"08:00:00"}
          slotMaxTime={"20:00:00"}
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
