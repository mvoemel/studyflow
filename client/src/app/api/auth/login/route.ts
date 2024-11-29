import { NextResponse } from "next/server";
import jwt from "jsonwebtoken";
import { mockToken } from "../../_mockdata/user-token-settings";
import { awaitTimeout } from "../../_utils";

const SECRET_KEY = process.env.JWT_SECRET || "tmp-secret-key";

export async function POST(request: Request) {
  const { email, password } = await request.json();

  await awaitTimeout(1000);

  if (email === "john@john.com" && password === "pass") {
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
