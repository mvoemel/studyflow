import { NextRequest, NextResponse } from "next/server";
import { mockSettings, mockUser } from "../../_mockdata/user-token-settings";
// import jwt from "jsonwebtoken";

// const SECRET_KEY = process.env.JWT_SECRET || "tmp-secret-key";

export async function GET(request: NextRequest) {
  const token = request.cookies.get("token");
  if (!token) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  // try {
  //   const user = jwt.verify(token.value, SECRET_KEY);
  //   return NextResponse.json({ user });
  // } catch (error) {
  //   console.error("Failed to verify token:", error);
  //   return NextResponse.json({ error: "Invalid token" }, { status: 401 });
  // }

  return NextResponse.json({ mockUser, mockSettings });
}
