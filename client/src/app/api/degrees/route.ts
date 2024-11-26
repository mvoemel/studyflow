import { NextResponse } from "next/server";
import { mockDegrees } from "../_mockdata/degrees";

//TODO: implement database connection and replace mock data

export async function GET() {
  return NextResponse.json(mockDegrees);
}
