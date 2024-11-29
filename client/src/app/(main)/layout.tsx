"use client";

import { SidebarProvider, SidebarInset } from "@/components/ui/sidebar";
import { ReactNode } from "react";
import { AppSidebar } from "@/components/sidebar/app-sidebar";
import { AppSidebarHeader } from "@/components/sidebar/app-sidebar-header";

type MainLayoutProps = {
  children: ReactNode;
};

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
    <SidebarProvider>
      <AppSidebar />
      <SidebarInset>
        <AppSidebarHeader />
        <div className="w-full h-full">{children}</div>
      </SidebarInset>
    </SidebarProvider>
  );
};

export default MainLayout;
