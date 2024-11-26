import { NextResponse } from "next/server";
import { mockSemester } from "../_mockdata/semesters";

//TODO: implement database connection and replace mock data

export async function GET() {
  return NextResponse.json(mockSemester);
}
