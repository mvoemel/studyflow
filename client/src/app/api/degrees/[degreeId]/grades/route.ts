import { mockGrades } from "@/app/api/_mockdata/grades";
import { NextResponse } from "next/server";

export async function GET() {
  return NextResponse.json(mockGrades);
}
