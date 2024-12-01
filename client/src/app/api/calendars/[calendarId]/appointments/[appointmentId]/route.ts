import { awaitTimeout } from "@/app/api/_utils";
import { NextRequest, NextResponse } from "next/server";

type Params = {
  calendarId: string;
  appointmentId: string;
};

export async function PATCH(request: NextRequest, context: { params: Params }) {
  const calendarId = context.params.calendarId;
  const appointmentId = context.params.appointmentId;

  const { title, description, startDate, endDate } = await request.json();

  await awaitTimeout(400);

  return NextResponse.json({ message: "Successfully updated appointment" });
}

export async function DELETE(
  request: NextRequest,
  context: { params: Params }
) {
  await awaitTimeout(500);

  return NextResponse.json({ message: "Successfully deleted appointment" });
}
