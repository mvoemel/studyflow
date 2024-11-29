import { NextResponse } from "next/server";

export async function PATCH(request: Request) {
  const { activeDegreeId } = await request.json();

  if (!activeDegreeId)
    return NextResponse.json({ message: "No degree provided", status: 400 });

  return NextResponse.json({ message: "Updated" });
}
