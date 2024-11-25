import { SidebarProvider, SidebarInset } from "@/components/ui/sidebar";
import { ReactNode } from "react";
import { AppSidebar } from "@/components/sidebar/app-sidebar";
import { DataProvider } from "@/providers/data-provider";
import { AppSidebarHeader } from "@/components/sidebar/app-sidebar-header";

type MainLayoutProps = {
  children: ReactNode;
};

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
    <DataProvider>
      <SidebarProvider>
        <AppSidebar />
        <SidebarInset>
          <AppSidebarHeader />
          <div className="w-full h-full">{children}</div>
        </SidebarInset>
      </SidebarProvider>
    </DataProvider>
  );
};

export default MainLayout;
