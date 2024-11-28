import { NextRequest, NextResponse } from "next/server";
import { mockDegrees } from "../_mockdata/degrees";

//TODO: implement database connection and replace mock data

export async function GET() {
  return NextResponse.json(mockDegrees);
}

export async function POST(request: NextRequest) {
  const { name, description } = await request.json();

  if (!name) return NextResponse.json({ message: "Invalid name", status: 400 });

  return NextResponse.json({
    id: "degree-123",
    name,
    userId: "user-1",
    description,
  });
}
