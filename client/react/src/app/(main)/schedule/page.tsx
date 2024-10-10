// TODO: implement
"use client"
import { useEffect, useState } from 'react';
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from "@fullcalendar/timegrid";
import interactionPlugin from '@fullcalendar/interaction'


const SchedulePage = () => {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        const fetchEvents = async () => {
            const response = await fetch('/api/events');
            const data = await response.json();
            setEvents(data);
        };

        fetchEvents();
    }, []);

  return (

      <FullCalendar
          plugins={[ timeGridPlugin,interactionPlugin ]}
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
  );
};

export default SchedulePage;
