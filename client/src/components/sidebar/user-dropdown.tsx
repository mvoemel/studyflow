"use client";

import {
  ChevronsUpDown,
  LogOut,
  MonitorIcon,
  MoonIcon,
  SunIcon,
  User2Icon,
} from "lucide-react";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuRadioGroup,
  DropdownMenuRadioItem,
  DropdownMenuSeparator,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from "@/components/ui/sidebar";
import { useTheme } from "next-themes";
import { MouseEvent } from "react";

type UserDropdownProps = {
  username: string;
  firstname: string;
  lastname: string;
  handleProfileClick: () => void;
  handleLogout: (e: MouseEvent<HTMLDivElement>) => Promise<void>;
};

const UserDropdown = ({
  username,
  firstname,
  lastname,
  handleProfileClick,
  handleLogout,
}: UserDropdownProps) => {
  const { isMobile } = useSidebar();
  const { theme, setTheme } = useTheme();

  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <SidebarMenuButton
              size="lg"
              className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
            >
              <Avatar className="h-8 w-8 rounded-lg">
                {/* TODO: either implement or discard */}
                {/* <AvatarImage src={user.avatar} alt={user.name} /> */}
                <AvatarFallback className="rounded-lg">
                  {firstname.charAt(0)}
                  {lastname.charAt(0)}
                </AvatarFallback>
              </Avatar>
              <div className="grid flex-1 text-left text-sm leading-tight">
                <span className="truncate font-semibold">{`${firstname} ${lastname}`}</span>
                <span className="truncate text-xs">{username}</span>
              </div>
              <ChevronsUpDown className="ml-auto size-4" />
            </SidebarMenuButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            className="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg"
            side={isMobile ? "bottom" : "right"}
            align="end"
            sideOffset={4}
          >
            <DropdownMenuLabel className="p-0 font-normal">
              <div className="flex items-center gap-2 px-1 py-1.5 text-left text-sm">
                <Avatar className="h-8 w-8 rounded-lg">
                  {/* TODO: either implement or discard */}
                  {/* <AvatarImage src={user.avatar} alt={user.name} /> */}
                  <AvatarFallback className="rounded-lg">
                    {firstname.charAt(0)}
                    {lastname.charAt(0)}
                  </AvatarFallback>
                </Avatar>
                <div className="grid flex-1 text-left text-sm leading-tight">
                  <span className="truncate font-semibold">{`${firstname} ${lastname}`}</span>
                  <span className="truncate text-xs">{username}</span>
                </div>
              </div>
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuGroup>
              <DropdownMenuItem onClick={handleProfileClick}>
                <User2Icon className="h-4 w-4" />
                <span className="ml-2">Profile</span>
              </DropdownMenuItem>
              <DropdownMenuSub>
                <DropdownMenuSubTrigger>
                  <SunIcon className="h-4 w-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
                  <MoonIcon className="absolute h-4 w-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
                  <span className="ml-2">Theme</span>
                </DropdownMenuSubTrigger>
                <DropdownMenuSubContent>
                  <DropdownMenuRadioGroup value={theme}>
                    <DropdownMenuRadioItem
                      key={"light"}
                      value={"light"}
                      onClick={() => setTheme("light")}
                    >
                      <SunIcon className="h-4 w-4 mr-2" />
                      Light
                    </DropdownMenuRadioItem>
                    <DropdownMenuRadioItem
                      key={"dark"}
                      value={"dark"}
                      onClick={() => setTheme("dark")}
                    >
                      <MoonIcon className="h-4 w-4 mr-2" />
                      Dark
                    </DropdownMenuRadioItem>
                    <DropdownMenuRadioItem
                      key={"system"}
                      value={"system"}
                      onClick={() => setTheme("system")}
                    >
                      <MonitorIcon className="h-4 w-4 mr-2" />
                      System
                    </DropdownMenuRadioItem>
                  </DropdownMenuRadioGroup>
                </DropdownMenuSubContent>
              </DropdownMenuSub>
            </DropdownMenuGroup>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={handleLogout}>
              <LogOut className="h-4 w-4" />
              <span className="ml-2">Log out</span>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
    </SidebarMenu>
  );
};

export { UserDropdown };