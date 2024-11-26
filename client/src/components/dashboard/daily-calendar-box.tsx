"use client";
import { useEffect, useState } from "react";
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
import { useData } from "@/providers/data-provider";

type DailyCalendarBoxProps = {
  className?: string;
};

const DailyCalendarBox = ({ className }: DailyCalendarBoxProps) => {
  const [events, setEvents] = useState([]);
  const { settings } = useData();

  // TODO: Adjust in future for database data
  useEffect(() => {
    if (settings?.activeDegreeId && settings?.activeSemesterId) {
      fetch(
        `/api/events?degreeId=${settings?.activeDegreeId}&semesterId=${settings?.activeSemesterId}`
      )
        .then((response) => response.json())
        .then((data) => setEvents(data))
        .catch((error) => console.error("Error fetching events:", error));
    }
  }, [settings]);

  return (
    <Card className={cn(className, "bg-muted/50")}>
      <CardHeader className="pb-3">
        <CardDescription>Schedule for Today</CardDescription>
      </CardHeader>
      <CardContent>
        <FullCalendar
          plugins={[timeGridPlugin, interactionPlugin]}
          initialView={"timeGridDay"}
          weekends={false}
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
        />
      </CardContent>
    </Card>
  );
};

export { DailyCalendarBox };
