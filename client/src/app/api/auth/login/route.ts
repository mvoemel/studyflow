import { NextResponse } from "next/server";
import jwt from "jsonwebtoken";

const SECRET_KEY = process.env.JWT_SECRET || "tmp-secret-key";

export async function POST(request: Request) {
  const { username, password } = await request.json();

  if (username === "john" && password === "pass") {
    const token = jwt.sign({ username }, SECRET_KEY, { expiresIn: "1h" });

    const response = NextResponse.json({ message: "Login successful" });
    response.cookies.set("token", token, { httpOnly: true, secure: true });

    return response;
  } else {
    return NextResponse.json(
      { message: "Invalid credentials" },
      { status: 401 }
    );
  }
}
