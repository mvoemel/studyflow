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
            fetch(`/api/events?degreeId=${selectedDegree.id}&semesterId=${activeSemester.id}`)
                .then(response => response.json())
                .then(data => setEvents(data))
                .catch(error => console.error('Error fetching events:', error));
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

      <div
          className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
          <h1 className="text-3xl font-semibold">Schedule Plan</h1>
          <div className="flex gap-4" >
              <Button onClick={openAddAppointmentDialog}>Add an Appointment</Button>
              <Button onClick={openCreateScheduleDialog}>Create a Schedule Plan</Button>
          </div>
          <Card className="p-7">
              <FullCalendar
                  plugins={[timeGridPlugin, interactionPlugin]}
                  initialView={"timeGridWeek"}
                  weekends={false}
                  nowIndicator={true}
                  events={events}
                  slotMinTime={'08:00:00'}
                  slotMaxTime={'20:00:00'}
                  slotLabelFormat={{
                      hour: '2-digit',
                      minute: '2-digit',
                      hour12: false
                  }}
                  eventTimeFormat={{
                      hour: '2-digit',
                      minute: '2-digit',
                      hour12: false
                  }}
                  editable={true}
              />
          </Card>
          <AddAppointmentDialog isOpen={isAddAppointmentDialogOpen} onClose={closeAddAppointmentDialog} />
            <CreateScheduleDialog isOpen={isCreateScheduleDialogOpen} onClose={closeCreateScheduleDialog} modules={filteredModules} />
      </div>
  );
};

export default SchedulePage;
