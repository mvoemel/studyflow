import {
    SidebarProvider,
    SidebarTrigger,
    SidebarInset,
} from "@/components/sidebar/sidebar"
import { ReactNode } from "react";
import { AppSidebar } from "@/components/sidebar/app-sidebar";
import { DegreeProvider } from "@/context/DegreeContext";
import { ModuleProvider } from "@/context/ModuleContext";


type MainLayoutProps = {
  children: ReactNode;
};

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
      <DegreeProvider>
          <ModuleProvider>
      <SidebarProvider>
              <AppSidebar />
                  <SidebarInset>
                      <SidebarTrigger />
                      <div className="w-full">
                        {children}
                      </div>
                  </SidebarInset>
      </SidebarProvider>
          </ModuleProvider>
      </DegreeProvider>
  );
};

export default MainLayout;
