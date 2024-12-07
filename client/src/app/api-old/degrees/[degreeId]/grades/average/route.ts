import { awaitTimeout } from "@/app/api-old/_utils";
import { NextResponse } from "next/server";

export async function GET() {
  await awaitTimeout(800);

  return NextResponse.json({ averageDegreeGrade: 5.3 });
}
