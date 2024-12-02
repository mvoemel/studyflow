import { NextResponse } from "next/server";
import { awaitTimeout } from "../../_utils";

export async function POST(request: Request) {
  const { firstname, lastname, username, password } = await request.json();
  console.log(
    "New User: " + firstname + " " + lastname + " " + username + " " + password
  );

  await awaitTimeout(2000);

  const response = NextResponse.json({ message: "Register successful" });

  return response;
}
