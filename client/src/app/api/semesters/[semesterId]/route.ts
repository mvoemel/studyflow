import { NextRequest, NextResponse } from "next/server";

type Params = {
  semesterId: string;
};

export async function PATCH(request: NextRequest, context: { params: Params }) {
  const semesterId = context.params.semesterId;
  const { name, description } = await request.json();

  return NextResponse.json({ message: "Updated" });
}
