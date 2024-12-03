import { NextRequest, NextResponse } from "next/server";
import { awaitTimeout } from "../../_utils";
import { mockCalendars } from "../../_mockdata/calendars";

type Params = {
  calendarId: string;
};

export async function GET(request: NextRequest, context: { params: Params }) {
  const calendarId = context.params.calendarId;

  await awaitTimeout(700);

  return NextResponse.json(mockCalendars.find((c) => c.id === calendarId));
}
