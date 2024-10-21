import { NextResponse } from 'next/server';

//TODO: implement database connection and repalce mock data
const modules = [
    {
        name: 'Web Development',
        ECTS: 4,
        Understanding: 8,
        Time: 7,
        Importance: 9,
    },
    {
        name: 'Software Development',
        ECTS: 4,
        Understanding: 7,
        Time: 6,
        Importance: 8,
    },
    {
        name: 'Algorithms and Data Structures',
        ECTS: 4,
        Understanding: 8,
        Time: 8,
        Importance: 10,
    }
];

export async function GET() {
    return NextResponse.json(modules);
}