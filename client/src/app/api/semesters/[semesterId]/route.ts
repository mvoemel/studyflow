import { NextRequest, NextResponse } from "next/server";
import { awaitTimeout } from "../../_utils";

type Params = {
  semesterId: string;
};

export async function PATCH(request: NextRequest, context: { params: Params }) {
  const semesterId = context.params.semesterId;
  const { name, description } = await request.json();

  await awaitTimeout(300);

  return NextResponse.json({ message: "Updated" });
}
