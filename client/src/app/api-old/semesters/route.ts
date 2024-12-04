import { NextRequest, NextResponse } from "next/server";
import { mockSemester } from "../_mockdata/semesters";
import { awaitTimeout } from "../_utils";

export async function GET() {
  await awaitTimeout(500);

  return NextResponse.json(mockSemester);
}

export async function POST(request: NextRequest) {
  const { name, degreeId, description } = await request.json();

  await awaitTimeout(1000);

  return NextResponse.json({
    id: "semester-17",
    name,
    description,
    degreeId,
    userId: "user-1",
  });
}
