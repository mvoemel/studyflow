import { NextRequest, NextResponse } from "next/server";
import { awaitTimeout } from "../../_utils";

type Params = {
  moduleId: string;
};

export async function PATCH(request: NextRequest, context: { params: Params }) {
  const moduleId = context.params.moduleId;
  const { name, ects, complexity, understanding, time, description } =
    await request.json();

  await awaitTimeout(600);

  return NextResponse.json({ message: "Updated" });
}

export async function DELETE(
  request: NextRequest,
  context: { params: Params }
) {
  await awaitTimeout(300);

  return NextResponse.json({ message: "Successfully deleted modules" });
}
