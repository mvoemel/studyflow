import { NextRequest, NextResponse } from "next/server";

export const middleware = async (request: NextRequest) => {
  const { nextUrl } = request;

  const token = request.cookies.get("secret");
  const validToken = token && !!token.value;

  if (
    !validToken &&
    (nextUrl.pathname.startsWith("/login") ||
      nextUrl.pathname.startsWith("/register"))
  ) {
    return NextResponse.next();
  }

  if (!validToken) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  if (
    validToken &&
    (nextUrl.pathname.startsWith("/login") ||
      nextUrl.pathname.startsWith("/register"))
  ) {
    return NextResponse.redirect(new URL("/dashboard", nextUrl));
  }
};

export const config = {
  matcher: [
    "/login",
    "/register",
    "/dashboard",
    "/grades",
    "/schedule",
    "/degree/:path*",
    "/profile",
  ],
};
