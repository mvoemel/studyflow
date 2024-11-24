import { SidebarProvider, SidebarInset } from "@/components/ui/sidebar";
import { ReactNode } from "react";
import { AppSidebar } from "@/components/sidebar/app-sidebar";
import { DegreeProvider } from "@/context/degree-context";
import AppSidebarHeader from "@/components/sidebar/app-sidebar-header";

type MainLayoutProps = {
  children: ReactNode;
};

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
    <DegreeProvider>
      <SidebarProvider>
        <AppSidebar />
        <SidebarInset>
          <AppSidebarHeader />
          <div className="w-full">{children}</div>
        </SidebarInset>
      </SidebarProvider>
    </DegreeProvider>
  );
};

export default MainLayout;
