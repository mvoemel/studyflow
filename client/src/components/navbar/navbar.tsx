"use client";

import Link from "next/link";
import {
  ChartNoAxesCombinedIcon,
  CircleUser,
  Menu,
  Search,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Input } from "@/components/ui/input";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { ThemeToggle } from "../global/theme-toggle";
import { useBasePath } from "./useBasePath";
import { navbarOptions } from "./options";
import { useRouter } from "next/navigation";

// TODO: implement user dropdown menu
// TODO: either keep search field or discard it
const Navbar = () => {
  const router = useRouter();
  const basePath = useBasePath();

  const handleLogout = async (e: { preventDefault: () => void }) => {
    e.preventDefault();

    const res = await fetch("/api/auth/logout", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: "",
    });

    if (res.ok) {
      router.push("/login");
    } else {
      alert("Logout failed");
    }
  };

  return (
    <header className="navbar sticky top-0 flex h-16 items-center gap-4 border-b bg-background px-4 md:px-6 z-50">
      <nav className="hidden flex-col gap-6 text-lg font-medium md:flex md:flex-row md:items-center md:gap-5 md:text-sm lg:gap-6">
        <Link
          href="/dashboard"
          className="flex items-center gap-2 text-lg font-semibold md:text-base"
        >
          <ChartNoAxesCombinedIcon className="h-6 w-6" />
          <span className="sr-only">Studyflow</span>
        </Link>
        {navbarOptions.map((option) => (
          <Link
            key={option.label}
            href={option.href}
            className={`${
              basePath !== option.href && "text-muted-foreground"
            } hover:text-foreground`}
          >
            {option.label}
          </Link>
        ))}
      </nav>
      <Sheet>
        <SheetTrigger asChild>
          <Button variant="outline" size="icon" className="shrink-0 md:hidden">
            <Menu className="h-5 w-5" />
            <span className="sr-only">Toggle navigation menu</span>
          </Button>
        </SheetTrigger>
        <SheetContent side="left">
          <nav className="grid gap-6 text-lg font-medium">
            <Link
              href="#"
              className="flex items-center gap-2 text-lg font-semibold"
            >
              <ChartNoAxesCombinedIcon className="h-6 w-6" />
              <span className="sr-only">StudyFlow</span>
            </Link>
            {navbarOptions.map((option) => (
              <Link
                key={option.label}
                href={option.href}
                className={`${
                  basePath !== option.href && "text-muted-foreground"
                } hover:text-foreground`}
              >
                {option.label}
              </Link>
            ))}
          </nav>
        </SheetContent>
      </Sheet>
      <div className="flex w-full items-center gap-4 md:ml-auto md:gap-2 lg:gap-4">
        <form className="ml-auto flex-1 sm:flex-initial">
          <div className="relative">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              type="search"
              placeholder="Search ..."
              className="pl-8 sm:w-[300px] md:w-[200px] lg:w-[300px]"
            />
          </div>
        </form>
        <ThemeToggle />
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="secondary" size="icon" className="rounded-full">
              <CircleUser className="h-5 w-5" />
              <span className="sr-only">Toggle user menu</span>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuLabel>My Account</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem>Settings</DropdownMenuItem>
            <DropdownMenuItem>Support</DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={handleLogout}>Logout</DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </header>
  );
};

export { Navbar };