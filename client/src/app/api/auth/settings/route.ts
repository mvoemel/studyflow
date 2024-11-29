import { NextResponse } from "next/server";
import { awaitTimeout } from "../../_utils";

export async function PATCH(request: Request) {
  const { activeDegreeId } = await request.json();

  await awaitTimeout(200);

  if (!activeDegreeId)
    return NextResponse.json({ message: "No degree provided", status: 400 });

  return NextResponse.json({ message: "Updated" });
}
