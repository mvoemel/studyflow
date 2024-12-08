"use client";

import { useState } from "react";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbList,
  BreadcrumbPage,
} from "../ui/breadcrumb";
import { Separator } from "../ui/separator";
import { SidebarTrigger } from "../ui/sidebar";
import { useBasePath } from "./use-base-path";
import { CheckCircle, CircleHelp } from "lucide-react";
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogClose,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

const AppSidebarHeader = () => {
  const basePath = useBasePath(); // ensure this is a valid hook and "use client" is at top
  const [openHelpModal, setOpenHelpModal] = useState(false);

  const instructions = [
    "Add a degree in the top left.",
    "Select the added degree (This sets it to your active degree).",
    "Add semesters to that degree in the sidebar under curriculum inside semesters.",
    "Set the desired semester to Active.",
    "Click on a semester and add modules to that semester.",
    "Finally, inside schedule you can add appointments and then create your schedule plan.",
  ];

  return (
    <header className="flex h-16 shrink-0 items-center justify-between gap-2 transition-[width,height] ease-linear group-has-[[data-collapsible=icon]]/sidebar-wrapper:h-12 px-4">
      <div className="flex items-center gap-2">
        <SidebarTrigger className="-ml-1" />
        <Separator orientation="vertical" className="mr-2 h-4" />
        {basePath && (
          <Breadcrumb>
            <BreadcrumbList>
              <BreadcrumbItem className="hidden md:block">
                <BreadcrumbPage className="text-xl">
                  {`${basePath?.charAt(1).toUpperCase()}${basePath?.slice(2)}`}
                </BreadcrumbPage>
              </BreadcrumbItem>
            </BreadcrumbList>
          </Breadcrumb>
        )}
      </div>

      <Dialog open={openHelpModal} onOpenChange={setOpenHelpModal}>
        <DialogTrigger asChild>
          <CircleHelp className="text-3xl cursor-pointer" />
        </DialogTrigger>
        <DialogContent className="max-w-md p-6 rounded-md shadow-lg space-y-6">
          <DialogHeader>
            <DialogTitle className="text-xl font-semibold">
              Getting Started
            </DialogTitle>
          </DialogHeader>

          <ol className="list-decimal list-inside space-y-4 pt-4">
            {instructions.map((step, index) => (
              <li key={index} className="flex items-start space-x-2">
                <CheckCircle className="text-green-500 h-5 w-5 flex-shrink-0 mt-0.5" />
                <span className="leading-6">{step}</span>
              </li>
            ))}
          </ol>

          <DialogClose asChild>
            <Button className="mt-4">Close</Button>
          </DialogClose>
        </DialogContent>
      </Dialog>
    </header>
  );
};

export { AppSidebarHeader };
