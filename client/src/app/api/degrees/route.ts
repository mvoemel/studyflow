import { Degree } from "@/types";
import { NextResponse } from "next/server";

//TODO: implement database connection and replace mock data
const degrees: Degree[] = [
  {
    id: "degree-1",
    name: "Bachelors Computer Science",
    description: "First Bachelor",
    userId: "user-1",
  },
  {
    id: "degree-2",
    name: "Masters Computer Science",
    description: "First Master",
    userId: "user-1",
  },
];

export async function GET() {
  return NextResponse.json(degrees);
}
