"use client";

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSkeleton,
  SidebarMenuSub,
} from "@/components/ui/sidebar";
import { navOptions } from "@/components/sidebar/options";
import { useBasePath } from "@/components/sidebar/use-base-path";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import {
  ChevronRight,
  PlusIcon,
  Book,
  MoreHorizontal,
  Edit,
  Trash2,
  CheckCircle,
} from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { MouseEvent, useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { AddDegreeDialog } from "@/components/dialogs/addDegree";
import { AddSemesterDialog } from "@/components/dialogs/addSemester";
import { DegreeDropdown } from "./degree-dropdown";
import { UserDropdown } from "./user-dropdown";
import { Degree, Semester } from "@/types";
import clsx from "clsx";
import { useUserSettings } from "@/hooks/use-user-settings";
import { useDegrees } from "@/hooks/use-degree";
import { useSemesters } from "@/hooks/use-semester";
import { logoutRequest } from "@/lib/api";
import { toast } from "sonner";
import { Badge } from "../ui/badge";

const AppSidebar = () => {
  const router = useRouter();
  const basePath = useBasePath();

  const { user, settings, updateActiveDegree } = useUserSettings();
  const { degrees, updateDegree } = useDegrees();
  const { semesters } = useSemesters();

  const activeSemesterId = useMemo(() => {
    if (!degrees || !semesters) return undefined;

    const currDegree = degrees?.find((d) => d.id === settings?.activeDegreeId);

    if (currDegree && currDegree.activeSemesterId) {
      return currDegree.activeSemesterId;
    }

    return undefined;
  }, [degrees, semesters, settings?.activeDegreeId]);

  useEffect(() => {
    console.log(activeSemesterId);
  }, [activeSemesterId]); // TODO: remove

  useEffect(() => {
    console.log(degrees);
    console.log(settings);
  }, [degrees, settings]); // TODO: remove

  const [isCollapsibleOpen, setIsCollapsibleOpen] = useState<boolean>(true);
  const [isAddDegreeDialogOpen, setIsAddDegreeDialogOpen] = useState(false);
  const [isAddSemesterDialogOpen, setIsAddSemesterDialogOpen] = useState(false);

  const openAddDegreeDialog = () => setIsAddDegreeDialogOpen(true);
  const closeAddDegreeDialog = () => setIsAddDegreeDialogOpen(false);

  const openAddSemesterDialog = () => setIsAddSemesterDialogOpen(true);
  const closeAddSemesterDialog = () => setIsAddSemesterDialogOpen(false);

  const handleSelectDegree = async (degree: Degree) => {
    if (settings?.activeDegreeId === degree.id) return;

    try {
      await updateActiveDegree({ activeDegreeId: degree.id });

      toast.success("Successfully updated active degree!");

      router.push("/dashboard");
    } catch (err) {
      toast.error("Failed to update active degree.");
    }
  };

  const handleSelectSemester = (semester: Semester) => {
    if (!settings?.activeDegreeId) return;

    router.push(
      `/degree/${settings?.activeDegreeId}/semester/${semester.id}/curriculum`
    );
  };

  const handleAddDegree = () => {
    openAddDegreeDialog();
  };

  const handleAddSemester = () => {
    openAddSemesterDialog();
  };

  const handleEditSemester = (semester: Semester) => {
    console.log("Edit Semester", semester);
    // TODO: check if request was successfull and then refetch
  };

  const handleDeleteSemester = (semester: Semester) => {
    console.log("Delete Semester", semester);
    // TODO: check if request was successfull and then refetch
  };

  const handleSetActiveSemester = async (semester: Semester) => {
    if (!settings?.activeDegreeId) return;

    const currDegree = degrees?.find((d) => d.id === settings.activeDegreeId);
    if (!currDegree) return;

    try {
      await updateDegree(
        { ...currDegree, activeSemesterId: semester.id },
        settings.activeDegreeId
      );

      toast.success("Successfully updated active semester!");

      router.push("/dashboard");
    } catch (err) {
      toast.error("Failed to update active semester.");
    }
  };

  const handleProfileClick = () => {
    router.push("/profile");
  };

  const handleLogout = async (e: MouseEvent<HTMLDivElement>) => {
    e.preventDefault();

    try {
      await logoutRequest();

      toast.success("Successfully logged out!");

      router.push("/dashboard");
    } catch (err) {
      toast.error("Failed to log out.");
    }
  };

  return (
    <Sidebar>
      <SidebarHeader>
        <SidebarMenu>
          <SidebarMenuItem>
            <DegreeDropdown
              degrees={degrees}
              selectedDegreeId={settings?.activeDegreeId}
              handleSelectDegree={handleSelectDegree}
              handleAddDegree={handleAddDegree}
            />
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>Studyflow</SidebarGroupLabel>
          <SidebarMenu>
            {navOptions.map((option) => (
              <SidebarMenuItem key={option.title}>
                <SidebarMenuButton
                  asChild
                  onClick={() => {
                    router.push(option.href);
                  }}
                  className={clsx("hover:text-foreground transition", {
                    "bg-sidebar-accent transition": basePath === option.href,
                  })}
                >
                  <div
                    className={clsx(
                      "hover:text-foreground transition cursor-pointer",
                      {
                        "bg-sidebar-accent transition":
                          basePath === option.href,
                      }
                    )}
                  >
                    <option.icon className="mr-2" />
                    {option.title}
                  </div>
                </SidebarMenuButton>
              </SidebarMenuItem>
            ))}
          </SidebarMenu>
        </SidebarGroup>

        <SidebarGroup>
          <SidebarGroupLabel>Curriculum</SidebarGroupLabel>
          <Collapsible
            open={isCollapsibleOpen}
            onOpenChange={setIsCollapsibleOpen}
            className="group/collapsible"
          >
            <CollapsibleTrigger asChild>
              <SidebarMenuButton>
                <Book className="mr-2" />
                Semesters
                <ChevronRight
                  className={`ml-auto transition-transform duration-200 ${
                    isCollapsibleOpen ? "rotate-90" : ""
                  }`}
                />
              </SidebarMenuButton>
            </CollapsibleTrigger>
            <CollapsibleContent>
              <SidebarMenuSub>
                <SidebarMenu>
                  {!semesters && !activeSemesterId ? (
                    <>
                      <SidebarMenuSkeleton />
                      <SidebarMenuSkeleton />
                      <SidebarMenuSkeleton />
                      <SidebarMenuSkeleton />
                    </>
                  ) : (
                    semesters
                      ?.filter(
                        (sem) => sem.degreeId === settings?.activeDegreeId
                      )
                      .map((semester) => (
                        <SidebarMenuItem
                          key={semester.id}
                          className="flex items-center justify-between space-x-2 pl-4"
                        >
                          <div
                            className={clsx(
                              "relative text-sm cursor-pointer pl-2 py-2 pr-6 rounded-md hover:bg-muted transition",
                              {
                                "font-bold": activeSemesterId === semester.id,
                                "text-blue-500":
                                  activeSemesterId === semester.id,
                              }
                            )}
                            onClick={(e) => {
                              e.preventDefault();
                              handleSelectSemester(semester);
                            }}
                          >
                            <span>{semester.name}</span>
                            {activeSemesterId === semester.id && (
                              <Badge
                                variant="secondary"
                                className="absolute top-0 right-0 h-4 px-1 text-blue-500"
                              >
                                Active
                              </Badge>
                            )}
                          </div>
                          <div className="flex items-center space-x-2">
                            <DropdownMenu>
                              <DropdownMenuTrigger asChild>
                                <Button variant="ghost" size="sm">
                                  <MoreHorizontal className="h-2 w-2 p-0" />
                                </Button>
                              </DropdownMenuTrigger>
                              <DropdownMenuContent>
                                <DropdownMenuItem
                                  onClick={() => handleEditSemester(semester)}
                                >
                                  <Edit className="mr-2 h-4 w-4" />
                                  Edit
                                </DropdownMenuItem>
                                <DropdownMenuItem
                                  onClick={() => handleDeleteSemester(semester)}
                                >
                                  <Trash2 className="mr-2 h-4 w-4" />
                                  Delete
                                </DropdownMenuItem>
                                <DropdownMenuItem
                                  onClick={() =>
                                    handleSetActiveSemester(semester)
                                  }
                                >
                                  <CheckCircle className="mr-2 h-4 w-4" />
                                  Set as Active
                                </DropdownMenuItem>
                              </DropdownMenuContent>
                            </DropdownMenu>
                          </div>
                        </SidebarMenuItem>
                      ))
                  )}
                  <SidebarMenuItem>
                    <Button
                      variant="ghost"
                      className="gap-2 p-2 w-full justify-start"
                      onClick={handleAddSemester}
                    >
                      <div className="flex size-6 items-center justify-center rounded-md border bg-background">
                        <PlusIcon className="size-4" />
                      </div>
                      <div className="font-medium text-muted-foreground">
                        Add Semester
                      </div>
                    </Button>
                  </SidebarMenuItem>
                </SidebarMenu>
              </SidebarMenuSub>
            </CollapsibleContent>
          </Collapsible>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter>
        <SidebarMenu>
          <SidebarMenuItem>
            <UserDropdown
              user={user}
              handleProfileClick={handleProfileClick}
              handleLogout={handleLogout}
            />
          </SidebarMenuItem>
        </SidebarMenu>
      </SidebarFooter>
      <AddDegreeDialog
        isOpen={isAddDegreeDialogOpen}
        onClose={closeAddDegreeDialog}
      />
      <AddSemesterDialog
        isOpen={isAddSemesterDialogOpen}
        onClose={closeAddSemesterDialog}
      />
    </Sidebar>
  );
};

export { AppSidebar };
