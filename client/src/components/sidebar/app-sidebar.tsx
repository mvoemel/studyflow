"use client"
import {
    Sidebar,
    SidebarContent, SidebarFooter,
    SidebarGroup,
    SidebarGroupLabel, SidebarHeader,
    SidebarMenu, SidebarMenuButton,
    SidebarMenuItem, SidebarMenuSub,
} from "@/components/sidebar/sidebar"
import { navbarOptions } from "@/components/sidebar/options";
import { useBasePath } from "@/components/sidebar/useBasePath";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import { ChevronDown, ChevronRight, ChevronUp, PlusIcon, User2, Book, MoreHorizontal, Edit, Trash2, CheckCircle } from "lucide-react";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { useDegree, Degree, Semester } from '@/context/DegreeContext';
import { useState } from 'react';
import { useRouter } from 'next/navigation';

export function AppSidebar() {
    const basePath = useBasePath();
    const { selectedDegree, setSelectedDegree, activeSemester, setActiveSemester, degrees } = useDegree();
    const [title, setTitle] = useState<string>("");
    const [isCollapsibleOpen, setIsCollapsibleOpen] = useState<boolean>(true);
    const [selectedSemester, setSelectedSemester] = useState<Semester | null>(null); // Track the selected semester
    const router = useRouter();

    const handleSelectDegree = (degree: Degree) => {
        setSelectedDegree(degree);
        const activeSem = degree.semesters.find(sem => sem.isActive);
        if (activeSem) {
            setActiveSemester(activeSem);
            if (window.location.pathname.includes("curriculum")) {
                router.push(`/degree/${degree.id}/semester/${activeSem.id}/curriculum`);
            }
        } else {
            const firstSemester = degree.semesters[0];
            setActiveSemester(firstSemester); // Set to the first semester if no active semester is found
            if (window.location.pathname.includes("curriculum")) {
                router.push(`/degree/${degree.id}/semester/${firstSemester.id}/curriculum`);
            }
        }
        fetch('/api/degree', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ degreeId: degree.id }),
        })
            .then(response => response.json())
            .catch(error => console.error('Error setting degree:', error));
    };

    const handleSelectSemester = (semester: Semester) => {
        setSelectedSemester(semester); // Set the clicked semester as the selected one
        if (selectedDegree) {
            setTitle(`Modules for ${semester.name} of ${selectedDegree.name}`);
            router.push(`/degree/${selectedDegree.id}/semester/${semester.id}/curriculum`);
        }
    };

    const handleAddDegree = () => {
        console.log("Add Degree");
    }

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

    return (
        <Sidebar>
            <SidebarHeader>
                <SidebarMenu>
                    <SidebarMenuItem>
                        <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                                <SidebarMenuButton>
                                    {selectedDegree?.name || "Select Degree"}
                                    <ChevronDown className="ml-auto"/>
                                </SidebarMenuButton>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent className="w-[--radix-popper-anchor-width]">
                                {degrees.map((degree) => (
                                    <DropdownMenuItem key={degree.id} onClick={() => handleSelectDegree(degree)}>
                                        <span>{degree.name}</span>
                                    </DropdownMenuItem>
                                ))}
                                <div className="my-2 border-t border-gray-300"></div>
                                <DropdownMenuItem asChild>
                                    <Button className="w-full bg-transparent flex items-center justify-start space-x-2 p-2 text-gray-700 hover:bg-gray-100">
                                        <PlusIcon className="h-4 w-4 text-gray-600" />
                                        <span>Add Degree</span>
                                    </Button>
                                </DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </SidebarMenuItem>
                </SidebarMenu>
            </SidebarHeader>
            <SidebarContent>
                <SidebarGroup>
                    <SidebarGroupLabel>Studyflow</SidebarGroupLabel>
                    <SidebarMenu>
                        {navbarOptions.map((option) => (
                            <SidebarMenuItem key={option.title}>
                                <SidebarMenuButton asChild>
                                    <a href={option.href} className={`hover:text-foreground ${basePath === option.href ? "font-bold" : ""}`}>
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
                                <ChevronRight className={`ml-auto transition-transform duration-200 ${isCollapsibleOpen ? "rotate-90" : ""}`} />
                            </SidebarMenuButton>
                        </CollapsibleTrigger>
                        <CollapsibleContent>
                            <SidebarMenuSub>
                                <SidebarMenu>
                                    {selectedDegree?.semesters.map((semester) => (
                                        <SidebarMenuItem key={semester.id} className="flex items-center justify-between space-x-2 pl-4">
                                            <a
                                                href="#"
                                                className={`cursor-pointer ${selectedSemester?.id === semester.id ? "font-bold" : ""}`} // Apply bold font to selected semester
                                                onClick={(e) => {
                                                    e.preventDefault();
                                                    handleSelectSemester(semester);
                                                }}
                                            >
                                                {semester.name}
                                            </a>
                                            <div className="flex items-center space-x-2">
                                                {activeSemester?.id === semester.id && <CheckCircle aria-label="Active" className="text-blue-600 h-4 w-4" />}
                                                <DropdownMenu>
                                                    <DropdownMenuTrigger asChild>
                                                        <Button variant="ghost">
                                                            <MoreHorizontal className="h-4 w-4" />
                                                        </Button>
                                                    </DropdownMenuTrigger>
                                                    <DropdownMenuContent>
                                                        <DropdownMenuItem onClick={() => handleEditSemester(semester)}>
                                                            <Edit className="mr-2 h-4 w-4" />
                                                            Edit
                                                        </DropdownMenuItem>
                                                        <DropdownMenuItem onClick={() => handleDeleteSemester(semester)}>
                                                            <Trash2 className="mr-2 h-4 w-4" />
                                                            Delete
                                                        </DropdownMenuItem>
                                                        <DropdownMenuItem onClick={() => handleSetActiveSemester(semester)}>
                                                            <CheckCircle className="mr-2 h-4 w-4" />
                                                            Set as Active
                                                        </DropdownMenuItem>
                                                    </DropdownMenuContent>
                                                </DropdownMenu>
                                            </div>
                                        </SidebarMenuItem>
                                    ))}
                                    <div className="my-2 border-t border-gray-300"></div>
                                    <SidebarMenuItem>
                                        <Button
                                            className="w-full bg-transparent flex items-center justify-start space-x-2 p-2 text-gray-700 hover:bg-gray-100"
                                            onClick={() => console.log("Add Semester clicked")}
                                        >
                                            <PlusIcon className="h-4 w-4 text-gray-600" />
                                            <span>Add Semester</span>
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
                        <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                                <SidebarMenuButton>
                                    <User2 /> Username
                                    <ChevronUp className="ml-auto" />
                                </SidebarMenuButton>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent
                                side="top"
                                className="w-[--radix-popper-anchor-width]"
                            >
                                <DropdownMenuItem>
                                    <span>Account</span>
                                </DropdownMenuItem>
                                <DropdownMenuItem>
                                    <span>Billing</span>
                                </DropdownMenuItem>
                                <DropdownMenuItem>
                                    <span>Sign out</span>
                                </DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </SidebarMenuItem>
                </SidebarMenu>
            </SidebarFooter>
        </Sidebar>
    )
}