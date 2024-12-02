"use client";

import { useCallback, useMemo, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from "@fullcalendar/interaction";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { AddAppointmentDialog } from "@/components/dialogs/add-appointment";
import { CreateScheduleDialog } from "@/components/dialogs/create-schedule";
import { Skeleton } from "@/components/ui/skeleton";
import { useEvents } from "@/hooks/use-events";
import { useDegrees } from "@/hooks/use-degree";
import { useUserSettings } from "@/hooks/use-user-settings";
import { useSemesters } from "@/hooks/use-semester";
import { useModules } from "@/hooks/use-modules";

const SchedulePage = () => {
  const { settings } = useUserSettings();
  const { degrees } = useDegrees();
  const { semesters } = useSemesters();
  const { modules } = useModules();
  const { events } = useEvents();

  // TODO: perferably remove this feature
  const filteredModules = useMemo(() => {
    if (!degrees || !semesters || !modules || !settings?.activeDegreeId)
      return [];

    const currDegree = degrees?.find((d) => d.id === settings.activeDegreeId);

    if (!currDegree || !currDegree.activeSemesterId) return [];

    const currSemester = semesters?.find(
      (s) => s.id === currDegree.activeSemesterId
    );

    if (!currSemester) return [];

    return (
      modules?.filter(
        (m) => m.degreeId === currDegree.id && m.semesterId === currSemester.id
      ) || []
    );
  }, [settings, degrees, semesters, modules]);

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

  if (!events) return <SchedulePageSkeleton />;

  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <div className="flex gap-4">
        <Button onClick={openAddAppointmentDialog}>Add an Appointment</Button>
        <Button onClick={openCreateScheduleDialog}>
          Create a Schedule Plan
        </Button>
      </div>
      <Card className="p-7 bg-muted/50">
        <FullCalendar
          plugins={[timeGridPlugin, interactionPlugin]}
          initialView={"timeGridWeek"}
          weekends={false}
          nowIndicator={true}
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
        // TODO: refactor so that exams are not sent to the server
        modules={filteredModules}
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
