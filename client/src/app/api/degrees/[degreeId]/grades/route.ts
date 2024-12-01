import { mockGrades } from "@/app/api/_mockdata/grades";
import { awaitTimeout } from "@/app/api/_utils";
import { NextResponse } from "next/server";

export async function GET() {
  await awaitTimeout(700);

  return NextResponse.json(mockGrades);
}
