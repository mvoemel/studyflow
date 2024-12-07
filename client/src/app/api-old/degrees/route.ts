import { NextRequest, NextResponse } from "next/server";
import { mockDegrees } from "../_mockdata/degrees";
import { awaitTimeout } from "../_utils";

export async function GET() {
  await awaitTimeout(1000);

  return NextResponse.json(mockDegrees);
}

export async function POST(request: NextRequest) {
  const { name, description } = await request.json();

  await awaitTimeout(2000);

  if (!name) return NextResponse.json({ message: "Invalid name", status: 400 });

  return NextResponse.json({
    id: "degree-123",
    name,
    userId: "user-1",
    description,
  });
}
