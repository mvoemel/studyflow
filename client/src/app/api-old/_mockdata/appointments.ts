import { Appointment } from "@/types";

const mockAppointments: Appointment[] = [
  {
    id: "appointment-1",
    title: "Meeting",
    startDateTime: new Date("2024-10-10T10:00:00"),
    endDateTime: new Date("2024-10-10T11:30:00"),
    calendarId: "calendar-1", // global calendar user-1
    description: "This is a meeting about the upcoming project",
  },
  {
    id: "appointment-2",
    title: "Meeting",
    startDateTime: new Date("2024-11-02T10:30:00"),
    endDateTime: new Date("2024-11-02T12:00:00"),
    calendarId: "calendar-1", // global calendar user-1
    description: "This is a meeting about the upcoming presentation",
  },
  {
    id: "appointment-3",
    title: "Project",
    startDateTime: new Date("2024-11-11T09:00:00"),
    endDateTime: new Date("2024-11-11T12:00:00"),
    calendarId: "calendar-2", // semester calendar user-1
    description: "This is a meeting about the upcoming project",
  },
  {
    id: "appointment-4",
    title: "Presentation",
    startDateTime: new Date("2024-01-04T10:00:00"),
    endDateTime: new Date("2024-01-04T11:30:00"),
    calendarId: "calendar-2", // semester calendar user-1
    description: "This is a meeting about the upcoming presentation",
  },
  {
    id: "appointment-5",
    title: "Meeting",
    startDateTime: new Date("2024-01-05T10:30:00"),
    endDateTime: new Date("2024-01-05T12:00:00"),
    calendarId: "calendar-2", // semester calendar user-1
    description: "This is a meeting about the upcoming presentation",
  },
  {
    id: "appointment-6",
    title: "Project",
    startDateTime: new Date("2024-01-06T09:00:00"),
    endDateTime: new Date("2024-01-06T12:00:00"),
    calendarId: "calendar-3", // semester calendar user-1
    description: "This is a meeting about the upcoming project",
  },
  {
    id: "appointment-7",
    title: "Presentation",
    startDateTime: new Date("2024-01-07T10:00:00"),
    endDateTime: new Date("2024-01-07T11:30:00"),
    calendarId: "calendar-3", // semester calendar user-1
    description: "This is a meeting about the upcoming presentation",
  },
  {
    id: "appointment-8",
    title: "Meeting",
    startDateTime: new Date("2024-01-08T10:30:00"),
    endDateTime: new Date("2024-01-08T12:00:00"),
    calendarId: "calendar-4", // semester calendar user-1
    description: "This is a meeting about the upcoming presentation",
  },
  {
    id: "appointment-9",
    title: "Project",
    startDateTime: new Date("2024-01-09T09:00:00"),
    endDateTime: new Date("2024-01-09T12:00:00"),
    calendarId: "calendar-4", // semester calendar user-1
    description: "This is a meeting about the upcoming project",
  },
  {
    id: "appointment-10",
    title: "Presentation",
    startDateTime: new Date("2024-01-10T10:00:00"),
    endDateTime: new Date("2024-01-10T11:30:00"),
    calendarId: "calendar-4", // semester calendar user-1
    description: "This is a meeting about the upcoming presentation",
  },
];

export { mockAppointments };
