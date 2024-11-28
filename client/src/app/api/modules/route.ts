import { NextRequest, NextResponse } from "next/server";
import { mockModules } from "../_mockdata/modules";

export async function GET() {
  return NextResponse.json(mockModules);
}

export async function POST(request: NextRequest) {
  const {
    name,
    ects,
    complexity,
    understanding,
    time,
    degreeId,
    semesterId,
    description,
  } = await request.json();

  return NextResponse.json({
    id: "module-9",
    userId: "user-1",
    degreeId,
    semesterId,
    name,
    description,
    ects,
    complexity,
    understanding,
    time,
  });
}
