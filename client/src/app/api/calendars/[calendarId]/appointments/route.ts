import { mockAppointments } from "@/app/api/_mockdata/appointments";
import { awaitTimeout } from "@/app/api/_utils";
import { NextRequest, NextResponse } from "next/server";

type Params = {
  calendarId: string;
};

export async function GET(request: NextRequest, context: { params: Params }) {
  const calendarId = context.params.calendarId;

  await awaitTimeout(400);

  return NextResponse.json(
    mockAppointments.filter((a) => a.calendarId === calendarId)
  );
}

export async function POST(request: NextRequest, context: { params: Params }) {
  const calendarId = context.params.calendarId;
  const { title, startDateTime, endDateTime, description } =
    await request.json();

  await awaitTimeout(600);

  if (!title || !startDateTime || !endDateTime)
    return NextResponse.json({ message: "Invalid input", status: 400 });

  return NextResponse.json({
    id: "appointment-123",
    title,
    startDateTime,
    endDateTime,
    calendarId,
    description,
  });
}
