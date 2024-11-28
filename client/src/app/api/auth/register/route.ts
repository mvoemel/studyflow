import { NextResponse } from "next/server";

const awaitTimeout = (delay: number) =>
  new Promise((resolve) => setTimeout(resolve, delay));

export async function POST(request: Request) {
  const { firstname, lastname, username, password } = await request.json();
  console.log(
    "New User: " + firstname + " " + lastname + " " + username + " " + password
  );

  await awaitTimeout(2000); // Simulate delay for database operations

  const response = NextResponse.json({ message: "Register successful" });

  return response;
}
