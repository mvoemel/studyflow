"use client"
import { useEffect, useState } from 'react';
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from '@fullcalendar/interaction'
import { useDegree } from '@/context/DegreeContext';


const SchedulePage = () => {
    const { selectedDegree, activeSemester } = useDegree();
    const [events, setEvents] = useState([]);

    // TODO: Adjust for future database implementation
    useEffect(() => {
        if (selectedDegree && activeSemester) {
            fetch(`/api/events?degreeId=${selectedDegree.id}&semesterId=${activeSemester.id}`)
                .then(response => response.json())
                .then(data => setEvents(data))
                .catch(error => console.error('Error fetching events:', error));
        }
    }, [selectedDegree, activeSemester]);

  return (

      <main className="">
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
      </main>
          );
          };

          export default SchedulePage;
