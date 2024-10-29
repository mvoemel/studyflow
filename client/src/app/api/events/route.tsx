import { NextResponse } from 'next/server';

const events = [
    { title: 'Höhere Mathematik 1', start: '2024-10-10T12:30:00', end: '2024-10-10T18:00:00' },
    { title: 'Software Entwicklung 1', start: '2024-10-10T08:00:00', end: '2024-10-10T12:00:00' }
];

export async function GET() {
    return NextResponse.json(events);
}