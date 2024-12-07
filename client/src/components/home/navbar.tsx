"use client";

import { useState } from "react";
import Link from "next/link";
import { ActivityIcon, ChevronRightIcon, MenuIcon, XIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  return (
    <nav className="shadow-lg static md:relative">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex justify-between">
          <div className="flex space-x-7">
            <div>
              <Link href="/" className="flex items-center py-4 px-2">
                <ActivityIcon />
              </Link>
            </div>
            <div className="hidden md:flex items-center space-x-1">
              <Link
                href="/#features"
                className="py-4 px-2 text-muted-foreground hover:text-muted-foreground/50 transition duration-300"
              >
                Features
              </Link>
              <Link
                href="/#testimonials"
                className="py-4 px-2 text-muted-foreground hover:text-muted-foreground/50 transition duration-300"
              >
                Testimonials
              </Link>
              <Link
                href="/#faq"
                className="py-4 px-2 text-muted-foreground hover:text-muted-foreground/50 transition duration-300"
              >
                FAQ
              </Link>
            </div>
          </div>
          <div className="hidden md:flex items-center space-x-3">
            <div className="gap-x-6 items-center justify-end mt-6 space-y-6 md:flex md:space-y-0 md:mt-0">
              <Link href="/login" className="block hover:text-gray-50">
                Login
              </Link>
              <Link
                href="/register"
                className="py-2.5 px-4 text-center rounded-full duration-150 flex items-center text-white bg-blue-600 hover:bg-blue-500 active:bg-blue-700"
              >
                Start now
                <ChevronRightIcon />
              </Link>
            </div>
          </div>
          <div className="md:hidden flex items-center">
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button variant="outline" size="icon" onClick={toggleMenu}>
                  {isOpen ? (
                    <XIcon className="h-6 w-6" />
                  ) : (
                    <MenuIcon className="h-6 w-6" />
                  )}
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent className="w-screen border-none">
                <div className="px-4 py-3 shadow-lg">
                  <DropdownMenuItem asChild>
                    <Link
                      href="/#features"
                      className="w-full justify-center py-2 text-lg"
                    >
                      Features
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link
                      href="/#testimonials"
                      className="w-full justify-center py-2 text-lg"
                    >
                      Testimonials
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem asChild>
                    <Link
                      href="/#faq"
                      className="w-full justify-center py-2 text-lg"
                    >
                      FAQ
                    </Link>
                  </DropdownMenuItem>
                  <DropdownMenuItem>
                    <Button className="w-full mt-2">Sign In</Button>
                  </DropdownMenuItem>
                </div>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </div>
      </div>
    </nav>
  );
};

export { Navbar };
