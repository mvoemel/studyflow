"use client";

import { SidebarProvider, SidebarInset } from "@/components/ui/sidebar";
import { ReactNode } from "react";
import { AppSidebar } from "@/components/sidebar/app-sidebar";
import { AppSidebarHeader } from "@/components/sidebar/app-sidebar-header";
import { preload } from "swr";
import {
  dashboardRequest,
  getAllDegreesRequest,
  getAllModulesRequest,
  getAllSemestersRequest,
  meRequest,
} from "@/lib/api";

type MainLayoutProps = {
  children: ReactNode;
};

const MainLayout = ({ children }: MainLayoutProps) => {
  preload("user-settings", meRequest);
  preload("degrees", getAllDegreesRequest);
  preload("semesters", getAllSemestersRequest);
  preload("modules", getAllModulesRequest);
  preload("ects-average", dashboardRequest);

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
