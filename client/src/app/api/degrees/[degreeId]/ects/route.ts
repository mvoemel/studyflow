import { NextResponse } from "next/server";

export async function GET() {
  return NextResponse.json({ currEcts: 124, totalEcts: 180 });
}
