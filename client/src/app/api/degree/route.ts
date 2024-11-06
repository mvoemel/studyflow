import { NextResponse } from 'next/server';

//TODO: implement database connection and replace mock data
const degrees = [
    {
        name: 'Bachelors Computer Science',
        id: 1,
        semesters: [
            { id: 1, name: "1. Semester", isActive: true },
            { id: 2, name: "2. Semester", isActive: false },
            { id: 3, name: "3. Semester", isActive: false },
            { id: 4, name: "4. Semester", isActive: false },
            { id: 5, name: "5. Semester", isActive: false },
            { id: 6, name: "6. Semester", isActive: false },
        ],
        isActive: true
    },
    {
        name: 'Masters Computer Science',
        id: 2,
        semesters: [
            { id: 1, name: "1. Semester", isActive: false },
            { id: 2, name: "2. Semester", isActive: false },
            { id: 3, name: "3. Semester", isActive: false },
            { id: 4, name: "4. Semester", isActive: true }
        ],
        isActive: false
    },
];

export async function GET() {
    return NextResponse.json(degrees);
}