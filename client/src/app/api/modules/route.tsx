import { NextResponse } from 'next/server';

//TODO: implement database connection and replace mock data
const modules = [
    {
        id: 1,
        degreeId: 1,
        semesterId: 1,
        name: 'Web Development',
        ECTS: 4,
        Understanding: 8,
        Time: 7,
        Importance: 9,
    },
    {
        id: 2,
        degreeId: 1,
        semesterId: 1,
        name: 'Software Development',
        ECTS: 4,
        Understanding: 7,
        Time: 6,
        Importance: 8,
    },
    {
        id: 3,
        degreeId: 1,
        semesterId: 1,
        name: 'Database Systems',
        ECTS: 4,
        Understanding: 9,
        Time: 8,
        Importance: 9,
    },
    {
        id: 4,
        degreeId: 1,
        semesterId: 1,
        name: 'Computer Networks',
        ECTS: 4,
        Understanding: 6,
        Time: 7,
        Importance: 7,
    },
    {
        id: 5,
        degreeId: 1,
        semesterId: 2,
        name: 'Algorithms and Data Structures',
        ECTS: 4,
        Understanding: 8,
        Time: 8,
        Importance: 10,
    },
    {
        id: 6,
        degreeId: 1,
        semesterId: 2,
        name: 'Operating Systems',
        ECTS: 4,
        Understanding: 7,
        Time: 7,
        Importance: 9,
    },
    {
        id: 7,
        degreeId: 1,
        semesterId: 2,
        name: 'Artificial Intelligence',
        ECTS: 4,
        Understanding: 9,
        Time: 8,
        Importance: 10,
    },
    {
        id: 8,
        degreeId: 1,
        semesterId: 2,
        name: 'Machine Learning',
        ECTS: 4,
        Understanding: 8,
        Time: 9,
        Importance: 10,
    }
];

export async function GET() {
    return NextResponse.json(modules);
}