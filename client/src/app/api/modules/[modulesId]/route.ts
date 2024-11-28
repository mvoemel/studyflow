import { NextRequest, NextResponse } from "next/server";

type Params = {
  moduleId: string;
};

export async function PATCH(request: NextRequest, context: { params: Params }) {
  const moduleId = context.params.moduleId;
  const { name, ects, complexity, understanding, time, description } =
    await request.json();

  return NextResponse.json({ message: "Updated" });
}
