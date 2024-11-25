import { NextRequest, NextResponse } from "next/server";
// import jwt from "jsonwebtoken";
import { Settings, User } from "@/types";

const user: User = {
  id: "user-1",
  email: "john.doe@example.com",
  firstname: "John",
  lastname: "Doe",
  settingsId: "settings-1",
};

const settings: Settings = {
  id: "settings-1",
  globalCalendarId: "calendar-1",
  activeDegreeId: "degree-1",
  activeSemesterId: "semester-1",
};

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

  return NextResponse.json({ user, settings });
}
