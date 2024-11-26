import { NextResponse } from "next/server";
import { mockModules } from "../_mockdata/modules";

//TODO: implement database connection and replace mock data

export async function GET() {
  return NextResponse.json(mockModules);
}
