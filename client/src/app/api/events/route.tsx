import { NextResponse } from 'next/server';

const events = [
    { title: 'Web Development', start: '2024-10-10T09:00:00', end: '2024-10-10T11:00:00', degreeId: 1, semesterId: 1 },
    { title: 'Software Development', start: '2024-10-11T10:00:00', end: '2024-10-11T12:00:00', degreeId: 1, semesterId: 1 },
    { title: 'Database Systems', start: '2024-10-15T13:00:00', end: '2024-10-15T15:00:00', degreeId: 1, semesterId: 1 },
    { title: 'Computer Networks', start: '2024-10-14T14:00:00', end: '2024-10-14T16:00:00', degreeId: 1, semesterId: 1 },
    { title: 'Web Development', start: '2024-10-17T09:00:00', end: '2024-10-17T11:00:00', degreeId: 1, semesterId: 1 },
    { title: 'Software Development', start: '2024-10-18T10:00:00', end: '2024-10-18T12:00:00', degreeId: 1, semesterId: 1 },
    { title: 'Database Systems', start: '2024-10-19T13:00:00', end: '2024-10-19T15:00:00', degreeId: 1, semesterId: 1 },
    { title: 'Computer Networks', start: '2024-10-24T14:00:00', end: '2024-10-24T16:00:00', degreeId: 1, semesterId: 1 }
];

export async function GET(request: Request) {
    const url = new URL(request.url);
    const degreeId = parseInt(url.searchParams.get('degreeId') || '0');
    const semesterId = parseInt(url.searchParams.get('semesterId') || '0');

    const filteredEvents = events.filter(event => event.degreeId === degreeId && event.semesterId === semesterId);
    return NextResponse.json(filteredEvents);
}