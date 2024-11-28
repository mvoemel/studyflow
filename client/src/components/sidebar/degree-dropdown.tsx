"use client";

import * as React from "react";
import { ChevronsUpDown, Plus, School2Icon } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from "@/components/ui/sidebar";
import { Degree } from "@/types";
import { Skeleton } from "../ui/skeleton";

type DegreeDropdownProps = {
  degrees: Degree[] | undefined;
  selectedDegreeId: Degree["id"] | undefined;
  handleSelectDegree: (degree: Degree) => void;
  handleAddDegree: () => void;
};

const DegreeDropdown = ({
  degrees,
  selectedDegreeId,
  handleSelectDegree,
  handleAddDegree,
}: DegreeDropdownProps) => {
  const { isMobile } = useSidebar();

  if (!degrees) return <DegreeDropdownSkeleton />;

  return (
    <SidebarMenu>
      <SidebarMenuItem>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <SidebarMenuButton
              size="lg"
              className="data-[state=open]:bg-sidebar-accent data-[state=open]:text-sidebar-accent-foreground"
            >
              <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-sidebar-primary text-sidebar-primary-foreground">
                <School2Icon className="size-4" />
              </div>
              <div className="grid flex-1 text-left text-sm leading-tight">
                <span className="truncate font-semibold">
                  {degrees?.find((d) => d.id === selectedDegreeId)?.name}
                </span>
              </div>
              <ChevronsUpDown className="ml-auto" />
            </SidebarMenuButton>
          </DropdownMenuTrigger>
          <DropdownMenuContent
            className="w-[--radix-dropdown-menu-trigger-width] min-w-56 rounded-lg"
            align="start"
            side={isMobile ? "bottom" : "right"}
            sideOffset={4}
          >
            <DropdownMenuLabel className="text-xs text-muted-foreground">
              Degrees
            </DropdownMenuLabel>
            {degrees?.map((degree) => (
              <DropdownMenuItem
                key={degree.name}
                onClick={() => handleSelectDegree(degree)}
                className="gap-2 p-2"
              >
                <div className="flex size-6 items-center justify-center rounded-sm border">
                  <School2Icon className="size-4 shrink-0" />
                </div>
                {degree.name}
              </DropdownMenuItem>
            ))}
            <DropdownMenuSeparator />
            <DropdownMenuItem className="gap-2 p-2" onClick={handleAddDegree}>
              <div className="flex size-6 items-center justify-center rounded-md border bg-background">
                <Plus className="size-4" />
              </div>
              <div className="font-medium text-muted-foreground">
                Add degree
              </div>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </SidebarMenuItem>
    </SidebarMenu>
  );
};

const DegreeDropdownSkeleton = () => {
  return (
    <div className="flex items-center space-x-4">
      <Skeleton className="h-12 w-12 rounded-md" />
      <Skeleton className="h-5 w-[150px]" />
    </div>
  );
};

export { DegreeDropdown };
