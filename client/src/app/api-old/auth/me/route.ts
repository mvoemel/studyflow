import { NextRequest, NextResponse } from "next/server";
import { mockSettings, mockUser } from "../../_mockdata/user-token-settings";
import { awaitTimeout } from "../../_utils";

export async function GET(request: NextRequest) {
  const token = request.cookies.get("secret");
  if (!token) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  await awaitTimeout(400);

  return NextResponse.json({ user: mockUser, settings: mockSettings });
}
