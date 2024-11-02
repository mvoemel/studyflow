"use client"
import {
    Sidebar,
    SidebarContent, SidebarFooter,
    SidebarGroup,
    SidebarGroupContent,
    SidebarGroupLabel, SidebarHeader,
    SidebarMenu, SidebarMenuButton,
    SidebarMenuItem, SidebarMenuSub,
} from "@/components/sidebar/sidebar"
import { navbarOptions } from "@/components/sidebar/options";
import { useBasePath } from "@/components/sidebar/useBasePath";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "@/components/ui/collapsible";
import { ChevronDown, ChevronRight, ChevronUp, PlusIcon, User2 } from "lucide-react";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";

const degreeOptions = [
    "Bachelors Computer Science",
    "Masters Computer Science",
]

export function AppSidebar() {
    const basePath = useBasePath();
    const [selectedDegree, setSelectedDegree] = useState("");

    useEffect(() => {
        fetch('/api/degree')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => setSelectedDegree(data.selectedDegree))
            .catch(error => console.error('Error fetching degree:', error));
    }, []);

    const handleSelectDegree = (degree: string) => {
        setSelectedDegree(degree);
        fetch('/api/degree', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ degree }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .catch(error => console.error('Error setting degree:', error));
    };

    const handleAddDegree = () => {
        console.log("Add Degree");
    }

    return (
        <Sidebar>
            <SidebarHeader>
                <SidebarMenu>
                    <SidebarMenuItem>
                        <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                                <SidebarMenuButton>
                                    {selectedDegree || "Select Degree"}
                                    <ChevronDown className="ml-auto"/>
                                </SidebarMenuButton>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent className="w-[--radix-popper-anchor-width]">
                                {degreeOptions.map((degree) => (
                                    <DropdownMenuItem key={degree} onClick={() => handleSelectDegree(degree)}>
                                        <span>{degree}</span>
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
                            <Collapsible
                                key={option.title}
                                asChild
                                className="group/collapsible"
                            >
                                <SidebarMenuItem>
                                    {option.items && option.items.length > 0 ? (
                                        <>
                                            <CollapsibleTrigger asChild>
                                                <SidebarMenuButton tooltip={option.title}>
                                                    <span>{option.title}</span>
                                                    <ChevronRight className="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90" />
                                                </SidebarMenuButton>
                                            </CollapsibleTrigger>
                                            <CollapsibleContent>
                                                <SidebarMenuSub>
                                                    {option.items.map((item) => (
                                                        <SidebarMenuItem key={item}>
                                                            <a href={`${basePath}${option.href}/${item}`}>{item}</a>
                                                        </SidebarMenuItem>
                                                    ))}
                                                </SidebarMenuSub>
                                            </CollapsibleContent>
                                        </>
                                    ) : (
                                        <SidebarMenuButton asChild>
                                            <a href={option.href} className="hover:text-foreground">
                                                <span>{option.title}</span>
                                            </a>
                                        </SidebarMenuButton>
                                    )}
                                </SidebarMenuItem>
                            </Collapsible>
                        ))}
                    </SidebarMenu>
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