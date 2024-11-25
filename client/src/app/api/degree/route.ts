import { NextResponse } from "next/server";

//TODO: implement database connection and replace mock data
const degrees = [
  {
    id: "1234",
    name: "Bachelors Computer Science",
    description: "First Bachelor",
    semesters: [
      {
        id: "1001",
        name: "1. Semester",
        description: "Cool Semester",
        modules: [],
      },
      {
        id: "1002",
        name: "2. Semester",
        description: "Stupid Semester",
        modules: [],
      },
      {
        id: "1003",
        name: "3. Semester",
        description: "Stupid Semester",
        modules: [],
      },
      {
        id: "1004",
        name: "4. Semester",
        description: "Stupid Semester",
        modules: [],
      },
      {
        id: "1005",
        name: "5. Semester",
        description: "Cool Semester",
        modules: [],
      },
      {
        id: "1006",
        name: "6. Semester",
        description: "Cool Semester",
        modules: [],
      },
    ],
  },
  {
    id: "5678",
    name: "Masters Computer Science",
    description: "First Master",
    semesters: [
      {
        id: "2001",
        name: "1. Semester",
        description: "Cool Semester",
        modules: [],
      },
      {
        id: "2002",
        name: "2. Semester",
        description: "Boring Semester",
        modules: [],
      },
      {
        id: "2003",
        name: "3. Semester",
        description: "Boring Semester",
        modules: [],
      },
      {
        id: "2004",
        name: "4. Semester",
        description: "Cool Semester",
        modules: [],
      },
    ],
  },
];

export async function GET() {
  return NextResponse.json(degrees);
}
