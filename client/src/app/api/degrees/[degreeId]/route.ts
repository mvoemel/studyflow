import { NextRequest, NextResponse } from "next/server";

type Params = {
  degreeId: string;
};

export async function PATCH(request: NextRequest, context: { params: Params }) {
  const degreeId = context.params.degreeId;
  const { name, activeSemesterId, description } = await request.json();

  return NextResponse.json({ message: "Updated" });
}
