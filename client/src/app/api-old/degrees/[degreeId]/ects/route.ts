import { awaitTimeout } from "@/app/api-old/_utils";
import { NextResponse } from "next/server";

export async function GET() {
  await awaitTimeout(500);

  return NextResponse.json({ currEcts: 124, totalEcts: 180 });
}
