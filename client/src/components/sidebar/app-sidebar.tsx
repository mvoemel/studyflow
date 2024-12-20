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
  UniversityIcon,
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
import { AddDegreeDialog } from "@/components/dialogs/add-degree";
import { AddSemesterDialog } from "@/components/dialogs/add-semester";
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
import { useSWRConfig } from "swr";
import { AddGradeDialog } from "@/components/dialogs/add-grade";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

const AppSidebar = () => {
  const { mutate } = useSWRConfig();

  const [selectedSemester, setSelectedSemester] = useState<
    Semester | undefined
  >(undefined);

  const router = useRouter();
  const basePath = useBasePath();

  const { user, settings, updateActiveDegree } = useUserSettings();
  const { degrees, updateDegree } = useDegrees();
  const { semesters, deleteSemester, updateSemester } = useSemesters();
  const [isAlertOpen, setIsAlertOpen] = useState(false);
  const [semesterToDelete, setSemesterToDelete] = useState<Semester | null>(
    null
  );

  const activeSemesterId = useMemo(() => {
    if (!degrees || !semesters) return undefined;

    const currDegree = degrees?.find((d) => d.id === settings?.activeDegreeId);

    if (currDegree && currDegree.activeSemesterId) {
      return currDegree.activeSemesterId;
    }

    return undefined;
  }, [degrees, semesters, settings?.activeDegreeId]);

  const [isCollapsibleOpen, setIsCollapsibleOpen] = useState<boolean>(true);
  const [isAddDegreeDialogOpen, setIsAddDegreeDialogOpen] =
    useState<boolean>(false);
  const [isAddSemesterDialogOpen, setIsAddSemesterDialogOpen] =
    useState<boolean>(false);
  const [isEditSemesterDialogOpen, setIsEditSemesterDialogOpen] =
    useState<boolean>(false);

  const handleSelectDegree = async (degree: Degree) => {
    if (settings?.activeDegreeId === degree.id) return;

    try {
      await updateActiveDegree({
        id: settings?.id || "",
        activeDegreeId: degree.id,
        globalCalendarId: settings?.globalCalendarId || "",
      });

      toast.success("Successfully updated active degree!");
    } catch (err) {
      toast.error("Failed to update active degree.");
    }
  };

  const handleSelectSemester = (semester: Semester) => {
    if (!settings?.activeDegreeId) return;
    console.log(semester);
    router.push(
      `/degree/${settings?.activeDegreeId}/semester/${semester.id}/curriculum`
    );
  };

  const handleCurrentDegreeClick = () => {
    if (!settings?.activeDegreeId) return;

    router.push(`/degree/${settings?.activeDegreeId}`);
  };

  const handleEditSemester = (semester: Semester) => {
    setSelectedSemester(semester);
    setIsEditSemesterDialogOpen(true);
  };

  const handleAddSemester = () => {
    setIsAddSemesterDialogOpen(true);
  };

  const handleDeleteSemester = async (semester: Semester) => {
    try {
      await deleteSemester(semester.id);

      toast.success("Successfully deleted semester!");
    } catch (err) {
      toast.error("Failed to delete semester.");
    }
  };

  const openDeleteDialog = (semester: Semester) => {
    setSemesterToDelete(semester);
    setIsAlertOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (!semesterToDelete) return;
    await handleDeleteSemester(semesterToDelete);
    setIsAlertOpen(false);
    setSemesterToDelete(null);
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

      // Clear cache
      mutate(() => true, undefined, { revalidate: false });

      toast.success("Successfully logged out!");

      router.push("/login");
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
              handleAddDegree={() => setIsAddDegreeDialogOpen(true)}
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
          <SidebarMenuButton
            onClick={(e) => {
              e.preventDefault();
              handleCurrentDegreeClick();
            }}
          >
            <UniversityIcon className="mr-2" />
            Degree
          </SidebarMenuButton>
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
                                  onClick={() => openDeleteDialog(semester)}
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
                            <AlertDialog
                              open={isAlertOpen}
                              onOpenChange={setIsAlertOpen}
                            >
                              <AlertDialogContent>
                                <AlertDialogHeader>
                                  <AlertDialogTitle>
                                    Are you sure you want to delete this
                                    semester?
                                  </AlertDialogTitle>
                                  <AlertDialogDescription>
                                    This action cannot be undone.
                                  </AlertDialogDescription>
                                </AlertDialogHeader>
                                <AlertDialogFooter>
                                  <AlertDialogCancel
                                    onClick={() => setIsAlertOpen(false)}
                                  >
                                    Cancel
                                  </AlertDialogCancel>
                                  <AlertDialogAction
                                    onClick={handleConfirmDelete}
                                  >
                                    Continue
                                  </AlertDialogAction>
                                </AlertDialogFooter>
                              </AlertDialogContent>
                            </AlertDialog>
                          </div>
                        </SidebarMenuItem>
                      ))
                  )}
                  <SidebarMenuItem>
                    <Button
                      variant="ghost"
                      className="gap-2 p-2 w-full justify-start"
                      onClick={() => handleAddSemester()}
                      disabled={!settings?.activeDegreeId}
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
        onClose={() => setIsAddDegreeDialogOpen(false)}
      />

      <AddSemesterDialog
        isOpen={isAddSemesterDialogOpen && !isEditSemesterDialogOpen}
        onClose={() => setIsAddSemesterDialogOpen(false)}
        isEdit={false}
      />
      <AddSemesterDialog
        isOpen={isEditSemesterDialogOpen}
        onClose={() => setIsEditSemesterDialogOpen(false)}
        isEdit={true}
        semester={selectedSemester}
      />
    </Sidebar>
  );
};

export { AppSidebar };
