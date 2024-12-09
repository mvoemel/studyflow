"use client";

import { useCallback, useMemo, useState } from "react";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin, {
  EventResizeStopArg,
} from "@fullcalendar/interaction";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { AppointmentDialog } from "@/components/dialogs/appointment-dialog";
import { CreateScheduleDialog } from "@/components/dialogs/create-schedule";
import { Skeleton } from "@/components/ui/skeleton";
import { useDegrees } from "@/hooks/use-degree";
import { useUserSettings } from "@/hooks/use-user-settings";
import { useSemesters } from "@/hooks/use-semester";
import { useAppointments } from "@/hooks/use-appointments";
import {
  EventClickArg,
  EventDropArg,
  EventSourceInput,
} from "@fullcalendar/core/index.js";
import { toast } from "sonner";
import { AppointmentFormValues } from "@/components/dialogforms/appointment-form";
import { Appointment } from "@/types";
import { useModules } from "@/hooks/use-modules";

const SchedulePage = () => {
  const { settings } = useUserSettings();
  const { degrees } = useDegrees();
  const { semesters } = useSemesters();
  const { modules } = useModules();

  const currSemester = useMemo(() => {
    if (!degrees || !semesters || !settings?.activeDegreeId) return undefined;

    const currDegree = degrees?.find((d) => d.id === settings.activeDegreeId);

    if (!currDegree || !currDegree.activeSemesterId) return undefined;

    const currSemester = semesters?.find(
      (s) => s.id === currDegree.activeSemesterId
    );

    return currSemester;
  }, [settings, degrees, semesters]);

  const hasModules = useMemo(() => {
    const modulesInCurrSemester =
      modules?.filter((m) => m.semesterId === currSemester?.id) || [];

    return modulesInCurrSemester.length > 0;
  }, [modules, currSemester]);

  const {
    appointments: globalAppointments,
    addNewAppointment: addNewAppointment,
    updateAppointment: updateGlobalAppointment,
    deleteAppointment: deleteGlobalAppointment,
  } = useAppointments(settings?.globalCalendarId);
  const {
    appointments: currSemesterAppointments,
    updateAppointment: updateActiveSemesterAppointment,
    deleteAppointment: deleteActiveSemesterAppointment,
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

  const [appointmentDialogOpen, setAppointmentDialogOpen] = useState(false);
  const [createScheduleDialogOpen, setCreateScheduleDialogOpen] =
    useState(false);

  const [selectedAppointment, setSelectedAppointment] = useState<
    Appointment | undefined
  >();

  const openAddAppointmentDialog = useCallback(() => {
    setAppointmentDialogOpen(true);
  }, []);
  const openCreateScheduleDialog = useCallback(() => {
    setCreateScheduleDialogOpen(true);
  }, []);
  const closeCreateScheduleDialog = useCallback(() => {
    setCreateScheduleDialogOpen(false);
  }, []);

  const handleAppointmentDelete = async () => {
    setAppointmentDialogOpen(false);

    const appointmentFromGlobal = globalAppointments?.find(
      (a) => a.id === selectedAppointment?.id
    );

    const appointmentFromCurrSemester = currSemesterAppointments?.find(
      (a) => a.id === selectedAppointment?.id
    );

    try {
      if (appointmentFromGlobal) {
        await deleteGlobalAppointment(appointmentFromGlobal.id);
      } else if (appointmentFromCurrSemester) {
        await deleteActiveSemesterAppointment(appointmentFromCurrSemester.id);
      } else {
        toast.error("Appointment not found!");
      }

      toast.success("Successfully deleted appointment.");
    } catch {
      toast.error("Failed to delete appointment.");
    }
  };

  const handleAppointmentSubmit = async (data: AppointmentFormValues) => {
    setAppointmentDialogOpen(false);

    const body = {
      title: data.title,
      description: data.description,
      startDateTime: new Date(data.startDateTime),
      endDateTime: new Date(data.endDateTime),
    };

    try {
      if (!selectedAppointment) {
        await addNewAppointment(body);
      } else {
        const appointmentFromGlobal = globalAppointments?.find(
          (a) => a.id === selectedAppointment.id
        );

        const appointmentFromCurrSemester = currSemesterAppointments?.find(
          (a) => a.id === selectedAppointment.id
        );

        if (appointmentFromGlobal) {
          await updateGlobalAppointment(body, appointmentFromGlobal.id);
        } else if (appointmentFromCurrSemester) {
          await updateActiveSemesterAppointment(
            body,
            appointmentFromCurrSemester.id
          );
        } else {
          toast.error("Appointment not found!");
          return;
        }
      }

      toast.success(
        `Successfully ${
          !selectedAppointment ? "added new" : "updated"
        } appointment!`
      );
    } catch (err) {
      toast.error(
        `Failed to ${
          !selectedAppointment ? "added new" : "updated"
        } appointment.`
      );
    }
  };

  const handleOnClickEvent = (arg: EventClickArg) => {
    const appointmentFromGlobal = globalAppointments?.find(
      (a) => a.id === arg.event.id
    );

    const appointmentFromCurrSemester = currSemesterAppointments?.find(
      (a) => a.id === arg.event.id
    );

    if (!appointmentFromGlobal && !appointmentFromCurrSemester) {
      toast.error("Appointment not found!");
      return;
    } else if (appointmentFromGlobal && appointmentFromCurrSemester) {
      toast.error("Duplicate appointment id!");
      return;
    } else if (!appointmentFromGlobal && appointmentFromCurrSemester) {
      setSelectedAppointment(appointmentFromCurrSemester);
      setAppointmentDialogOpen(true);
      return;
    } else if (appointmentFromGlobal && !appointmentFromCurrSemester) {
      setSelectedAppointment(appointmentFromGlobal);
      setAppointmentDialogOpen(true);
      return;
    } else {
      toast.error("Something went wrong!");
      return;
    }
  };

  const handleOnDropEvent = async (arg: EventDropArg | EventResizeStopArg) => {
    const calendarId = arg.event.extendedProps.calendarId;
    const appointmentId = arg.event.id;
    const title = arg.event.title;
    const description = arg.event.extendedProps.description;
    const startDateTime = arg.event.start;
    const endDateTime = arg.event.end;

    if (!startDateTime || !endDateTime) return;

    const body = {
      title,
      description,
      startDateTime,
      endDateTime,
    };

    try {
      switch (calendarId) {
        case settings?.globalCalendarId:
          await updateGlobalAppointment(body, appointmentId);
          break;
        case currSemester?.calendarId:
          await updateActiveSemesterAppointment(body, appointmentId);
          break;
        default:
          throw new Error("Invalid calendar ID:", calendarId);
      }

      toast.success("Successfully updated appointment!");
    } catch {
      toast.error("Failed to update appointment.");
    }
  };

  if (!events) return <SchedulePageSkeleton />;

  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <div className="flex gap-4">
        <Button
          onClick={() => {
            setSelectedAppointment(undefined);
            openAddAppointmentDialog();
          }}
        >
          Add an Appointment
        </Button>
        <Button
          onClick={openCreateScheduleDialog}
          disabled={!!currSemester?.calendarId || !hasModules}
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
          eventClick={handleOnClickEvent}
          eventDragMinDistance={5}
          eventDrop={handleOnDropEvent}
          eventResize={handleOnDropEvent}
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

      <AppointmentDialog
        open={appointmentDialogOpen}
        onOpenChange={setAppointmentDialogOpen}
        onSubmit={handleAppointmentSubmit}
        // @ts-ignore
        initialData={selectedAppointment}
        onDelete={handleAppointmentDelete}
      />

      <CreateScheduleDialog
        isOpen={createScheduleDialogOpen}
        onClose={closeCreateScheduleDialog}
        semesterId={currSemester?.id}
        settingsId={settings?.id}
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
