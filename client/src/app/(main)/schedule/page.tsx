"use client"
import {useCallback, useEffect, useState} from 'react';
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from '@fullcalendar/interaction'
import { useDegree } from '@/context/DegreeContext';
import { useModule } from '@/context/ModuleContext';
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { AddAppointmentDialog } from "@/components/dialogs/addAppointment";
import { CreateScheduleDialog } from "@/components/dialogs/createSchedule";



const SchedulePage = () => {
    const { selectedDegree, activeSemester } = useDegree();
    const { modules } = useModule();
    const [events, setEvents] = useState([]);
    const [isAddAppointmentDialogOpen, setIsAddAppointmentDialogOpen] = useState(false);
    const [isCreateScheduleDialogOpen, setIsCreateScheduleDialogOpen] = useState(false);

  // TODO: Adjust for future database implementation
  useEffect(() => {
    if (selectedDegree && activeSemester) {
      fetch(
        `/api/events?degreeId=${selectedDegree.id}&semesterId=${activeSemester.id}`
      )
        .then((response) => response.json())
        .then((data) => setEvents(data))
        .catch((error) => console.error("Error fetching events:", error));
    }
  }, [selectedDegree, activeSemester]);

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

    const filteredModules = modules.filter(module =>
        module.degreeId === selectedDegree?.id && module.semesterId === activeSemester?.id
    );

  return (
    <main className="">
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
    </main>
  );
};

export default SchedulePage;
