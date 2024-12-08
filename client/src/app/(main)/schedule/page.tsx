"use client";

import { useCallback, useMemo, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin, {
  EventDragStartArg,
  EventDragStopArg,
  EventResizeStopArg,
} from "@fullcalendar/interaction";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { AddAppointmentDialog } from "@/components/dialogs/add-appointment";
import { CreateScheduleDialog } from "@/components/dialogs/create-schedule";
import { Skeleton } from "@/components/ui/skeleton";
import { useDegrees } from "@/hooks/use-degree";
import { useUserSettings } from "@/hooks/use-user-settings";
import { useSemesters } from "@/hooks/use-semester";
import { useAppointments } from "@/hooks/use-appointments";
import { EventClickArg, EventSourceInput } from "@fullcalendar/core/index.js";

const SchedulePage = () => {
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

  const {
    appointments: globalAppointments,
    updateAppointment: updateGlobalAppointment,
  } = useAppointments(settings?.globalCalendarId);
  const {
    appointments: currSemesterAppointments,
    updateAppointment: updateActiveSemesterAppointment,
  } = useAppointments(currSemester?.calendarId);

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

  const [isAddAppointmentDialogOpen, setIsAddAppointmentDialogOpen] =
    useState(false);
  const [isCreateScheduleDialogOpen, setIsCreateScheduleDialogOpen] =
    useState(false);

  const openAddAppointmentDialog = useCallback(() => {
    setIsAddAppointmentDialogOpen(true);
  }, []);
  const closeAddAppointmentDialog = useCallback(() => {
    setIsAddAppointmentDialogOpen(false);
  }, []);
  const openCreateScheduleDialog = useCallback(() => {
    setIsCreateScheduleDialogOpen(true);
  }, []);
  const closeCreateScheduleDialog = useCallback(() => {
    setIsCreateScheduleDialogOpen(false);
  }, []);

  const handleOnDClickEvent = (arg: EventClickArg) => {
    console.log("Clicked event:", arg);
  };

  const handleOnDragStopEvent = (arg: EventDragStopArg) => {
    console.log("Drag start event:", arg.event);
  };

  const handleOnResizeStopEvent = (arg: EventResizeStopArg) => {
    console.log("Resize stop event:", arg.event);
  };

  if (!events) return <SchedulePageSkeleton />;

  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <div className="flex gap-4">
        <Button onClick={openAddAppointmentDialog}>Add an Appointment</Button>
        <Button
          onClick={openCreateScheduleDialog}
          disabled={!!currSemester?.calendarId}
        >
          Create a Schedule Plan
        </Button>
      </div>
      <Card className="p-7 bg-muted/50">
        <FullCalendar
          plugins={[timeGridPlugin, interactionPlugin]}
          initialView="timeGridWeek"
          weekends={true}
          nowIndicator={true}
          allDaySlot={false}
          events={events}
          eventClick={handleOnDClickEvent}
          eventDragMinDistance={5}
          eventDragStop={handleOnDragStopEvent}
          eventResizeStop={handleOnResizeStopEvent}
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
          editable={true}
        />
      </Card>

      <AddAppointmentDialog
        isOpen={isAddAppointmentDialogOpen}
        onClose={closeAddAppointmentDialog}
      />

      <CreateScheduleDialog
        isOpen={isCreateScheduleDialogOpen}
        onClose={closeCreateScheduleDialog}
      />
    </div>
  );
};

const SchedulePageSkeleton = () => {
  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <div className="flex gap-4">
        <Skeleton className="h-10 w-[100px]" />
        <Skeleton className="h-10 w-[100px]" />
      </div>
      <Card className="p-7 bg-muted/50">
        <Skeleton className="h-[600px] w-full" />
      </Card>
    </div>
  );
};

export default SchedulePage;
