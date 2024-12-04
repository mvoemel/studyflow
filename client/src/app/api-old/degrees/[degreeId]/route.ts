import { NextRequest, NextResponse } from "next/server";
import { awaitTimeout } from "../../_utils";

type Params = {
  degreeId: string;
};

export async function PATCH(request: NextRequest, context: { params: Params }) {
  const degreeId = context.params.degreeId;
  const { name, activeSemesterId, description } = await request.json();

  await awaitTimeout(300);

  return NextResponse.json({ message: "Updated" });
}

export async function DELETE(
  request: NextRequest,
  context: { params: Params }
) {
  await awaitTimeout(800);

  return NextResponse.json({ message: "Successfully deleted degree" });
}
