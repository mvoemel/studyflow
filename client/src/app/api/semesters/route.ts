import { NextRequest, NextResponse } from "next/server";
import { mockSemester } from "../_mockdata/semesters";

export async function GET() {
  return NextResponse.json(mockSemester);
}

export async function POST(request: NextRequest) {
  const { name, degreeId, description } = await request.json();
  return NextResponse.json({
    id: "semester-17",
    name,
    description,
    degreeId,
    userId: "user-1",
  });
}
