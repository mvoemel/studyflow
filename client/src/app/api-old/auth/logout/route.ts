import { NextResponse } from "next/server";
import { awaitTimeout } from "../../_utils";

export async function POST() {
  const response = NextResponse.json({ message: "Logged out successfully" });

  await awaitTimeout(500);

  response.cookies.delete("secret");
  return response;
}
