import { NextResponse } from "next/server";
import jwt from "jsonwebtoken";
import { mockToken } from "../../_mockdata/user-token-settings";

const SECRET_KEY = process.env.JWT_SECRET || "tmp-secret-key";

const awaitTimeout = (delay: number) =>
  new Promise((resolve) => setTimeout(resolve, delay));

export async function POST(request: Request) {
  const { username, password } = await request.json();

  await awaitTimeout(1000); // Simulate delay for database operations

  if (username === "john" && password === "pass") {
    const token = jwt.sign(mockToken, SECRET_KEY, { expiresIn: "1h" });

    const response = NextResponse.json({ message: "Login successful" });
    response.cookies.set("token", token, {
      httpOnly: true,
      secure: true,
      maxAge: 60 * 60,
    });

    return response;
  } else {
    return NextResponse.json(
      { message: "Invalid credentials" },
      { status: 401 }
    );
  }
}
