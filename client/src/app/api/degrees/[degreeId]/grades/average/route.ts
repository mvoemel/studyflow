import { NextResponse } from "next/server";

export async function GET() {
  return NextResponse.json({ averageDegreeGrade: 5.3 });
}
