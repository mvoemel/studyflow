"use client"
import { useEffect, useState } from 'react';
import {
    Card,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from "@fullcalendar/interaction";
import { useDegree } from "@/context/DegreeContext";

type DailyCalendarBoxProps = {
    className?: string;
};

const DailyCalendarBox = ({ className }: DailyCalendarBoxProps) => {

    const [events, setEvents] = useState([]);
    const { selectedDegree, activeSemester } = useDegree();

    // TODO: Adjust in future for database data
    useEffect(() => {
        if (selectedDegree && activeSemester) {
            fetch(`/api/events?degreeId=${selectedDegree.id}&semesterId=${activeSemester.id}`)
                .then(response => response.json())
                .then(data => setEvents(data))
                .catch(error => console.error('Error fetching events:', error));
        }
    }, [selectedDegree, activeSemester]);

    return (
        <Card className={cn(className)}>
            <CardHeader className="pb-3">
                <CardTitle>Schedule for Today</CardTitle>
                <CardDescription className="max-w-lg text-balance leading-relaxed">
                </CardDescription>
                <FullCalendar
                    plugins={[ timeGridPlugin, interactionPlugin ]}
                    initialView={"timeGridDay"}
                    weekends={false}
                    nowIndicator={true}
                    editable={true}
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
                />
            </CardHeader>
        </Card>
    );
};

export { DailyCalendarBox };