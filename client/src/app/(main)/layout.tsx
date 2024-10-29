import {
    Sidebar,
    SidebarProvider,
    SidebarTrigger,
    SidebarContent,
    SidebarGroup,
    SidebarGroupContent,
    SidebarGroupLabel,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem, SidebarInset,
} from "@/components/sidebar/sidebar"
import { ReactNode } from "react";
import { AppSidebar } from "@/components/sidebar/app-sidebar";
import {Navbar} from "@/components/navbar/navbar";


type MainLayoutProps = {
  children: ReactNode;
};

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
      <SidebarProvider>
              <AppSidebar />
                  <SidebarInset>
                      <SidebarTrigger />
                      <div className="w-full">
                        {children}
                      </div>
                  </SidebarInset>
      </SidebarProvider>
  );
};

export default MainLayout;
