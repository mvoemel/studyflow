import { NextRequest, NextResponse } from "next/server";
import { jwtVerify } from "jose";

const SECRET_KEY = new TextEncoder().encode(
  process.env.JWT_SECRET || "tmp-secret-key"
);

export const middleware = async (request: NextRequest) => {
  const { nextUrl } = request;

  const token = request.cookies.get("token");

  if (!token && nextUrl.pathname.startsWith("/login")) {
    return NextResponse.next();
  }

  if (!token) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  if (token && nextUrl.pathname.startsWith("/login")) {
    return NextResponse.redirect(new URL("/dashboard", nextUrl));
  }

  // TODO: remove this
  try {
    const { payload } = await jwtVerify(token.value, SECRET_KEY);

    const response = NextResponse.next();
    response.headers.set("x-user", JSON.stringify(payload));

    return response;
  } catch (err) {
    console.log("Failed to verify JWT:", err);
    return NextResponse.redirect(new URL("/login", request.url));
  }
};

export const config = {
  matcher: [
    "/login",
    "/register",
    "/dashboard",
    "/grades",
    "/schedule",
    "/settings/:path*",
  ],
};
