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

type DailyCalendarBoxProps = {
    className?: string;
};

const DailyCalendarBox = ({ className }: DailyCalendarBoxProps) => {
    const [events, setEvents] = useState([]);

    // TODO: Adjust in future for database data
    useEffect(() => {
        const fetchEvents = async () => {
            const response = await fetch('/api/events');
            const data = await response.json();
            setEvents(data);
        };

        fetchEvents();
    }, []);

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