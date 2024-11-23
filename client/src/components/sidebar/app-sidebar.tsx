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
  SidebarMenuSub,
} from "@/components/ui/sidebar";
import { navOptions } from "@/components/sidebar/options";
import { useBasePath } from "@/components/sidebar/useBasePath";
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
import { useDegree, Degree, Semester } from "@/context/degree-context";
import { MouseEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { AddDegreeDialog } from "@/components/dialogs/addDegree";
import { AddSemesterDialog } from "@/components/dialogs/addSemester";
import { DegreeDropdown } from "./degree-dropdown";
import { UserDropdown } from "./user-dropdown";
import { useUser } from "@/hooks/use-user";
import clsx from "clsx";

const AppSidebar = () => {
  const basePath = useBasePath();
  const {
    selectedDegree,
    setSelectedDegree,
    activeSemester,
    setActiveSemester,
    degrees,
  } = useDegree();
  const { user } = useUser();
  const [isCollapsibleOpen, setIsCollapsibleOpen] = useState<boolean>(true);
  const [selectedSemester, setSelectedSemester] = useState<Semester | null>(
    null
  );
  const router = useRouter();
  const [isAddDegreeDialogOpen, setIsAddDegreeDialogOpen] = useState(false);
  const [isAddSemesterDialogOpen, setIsAddSemesterDialogOpen] = useState(false);

  const openAddDegreeDialog = () => setIsAddDegreeDialogOpen(true);
  const closeAddDegreeDialog = () => setIsAddDegreeDialogOpen(false);

  const openAddSemesterDialog = () => setIsAddSemesterDialogOpen(true);
  const closeAddSemesterDialog = () => setIsAddSemesterDialogOpen(false);

  const handleSelectDegree = (degree: Degree) => {
    setSelectedDegree(degree);
    const activeSem = degree.semesters.find((sem) => sem.isActive);
    if (activeSem) {
      setActiveSemester(activeSem);
      if (window.location.pathname.includes("curriculum")) {
        router.push(`/degree/${degree.id}/semester/${activeSem.id}/curriculum`);
      }
    } else {
      const firstSemester = degree.semesters[0];
      setActiveSemester(firstSemester); // Set to the first semester if no active semester is found
      if (window.location.pathname.includes("curriculum")) {
        router.push(
          `/degree/${degree.id}/semester/${firstSemester.id}/curriculum`
        );
      }
    }
    fetch("/api/degree", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ degreeId: degree.id }),
    })
      .then((response) => response.json())
      .catch((error) => console.error("Error setting degree:", error));
  };

  const handleSelectSemester = (semester: Semester) => {
    setSelectedSemester(semester); // Set the clicked semester as the selected one
    if (selectedDegree) {
      router.push(
        `/degree/${selectedDegree.id}/semester/${semester.id}/curriculum`
      );
    }
  };

  const handleAddDegree = () => {
    openAddDegreeDialog();
  };

  const handleAddSemester = () => {
    openAddSemesterDialog();
  };

  const handleEditSemester = (semester: Semester) => {
    console.log("Edit Semester", semester);
  };

  const handleDeleteSemester = (semester: Semester) => {
    console.log("Delete Semester", semester);
  };

  const handleSetActiveSemester = (semester: Semester) => {
    setActiveSemester(semester);
    console.log("Set Active Semester", semester);
  };

  const handleProfileClick = () => {
    router.push("/profile");
  };

  const handleLogout = async (e: MouseEvent<HTMLDivElement>) => {
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
    <Sidebar>
      <SidebarHeader>
        <SidebarMenu>
          <SidebarMenuItem>
            <DegreeDropdown
              degrees={degrees}
              selectedDegree={selectedDegree}
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
                <SidebarMenuButton asChild>
                  <a
                    href={option.href}
                    className={clsx("hover:text-foreground transition", {
                      "bg-sidebar-accent transition": basePath === option.href,
                    })}
                  >
                    <option.icon className="mr-2" />
                    {option.title}
                  </a>
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
                  {selectedDegree?.semesters.map((semester) => (
                    <SidebarMenuItem
                      key={semester.id}
                      className="flex items-center justify-between space-x-2 pl-4"
                    >
                      <a
                        href="#"
                        className={`cursor-pointer ${
                          selectedSemester?.id === semester.id
                            ? "font-bold"
                            : ""
                        }`}
                        onClick={(e) => {
                          e.preventDefault();
                          handleSelectSemester(semester);
                        }}
                      >
                        {semester.name}
                      </a>
                      <div className="flex items-center space-x-2">
                        {activeSemester?.id === semester.id && (
                          <CheckCircle
                            aria-label="Active"
                            className="text-blue-600 h-4 w-4"
                          />
                        )}
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost">
                              <MoreHorizontal className="h-4 w-4" />
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
                              onClick={() => handleSetActiveSemester(semester)}
                            >
                              <CheckCircle className="mr-2 h-4 w-4" />
                              Set as Active
                            </DropdownMenuItem>
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </div>
                    </SidebarMenuItem>
                  ))}
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
            {/* TODO: check how to handle type checking here */}
            <UserDropdown
              username={user?.username ?? ""}
              firstname={user?.firstname ?? ""}
              lastname={user?.lastname ?? ""}
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
