import { NextRequest, NextResponse } from "next/server";
import { mockModules } from "../_mockdata/modules";
import { awaitTimeout } from "../_utils";

export async function GET() {
  await awaitTimeout(350);

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

  await awaitTimeout(1500);

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
