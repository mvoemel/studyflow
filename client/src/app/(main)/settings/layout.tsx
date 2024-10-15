"use client";

import Link from "next/link";
import { ReactNode } from "react";
import { usePathname } from "next/navigation";

type SettingsLayoutProps = {
    children: ReactNode;
};

// TODO: implement links and navigation for settings
const SettingsLayout = ({ children }: SettingsLayoutProps) => {
    const pathname = usePathname();
    return (
        <main className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
            <div className="mx-auto grid w-full max-w-6xl gap-2">
                <h1 className="text-3xl font-semibold">Settings</h1>
            </div>
            <div className="mx-auto grid w-full max-w-6xl items-start gap-6 md:grid-cols-[180px_1fr] lg:grid-cols-[250px_1fr]">
                <nav className="grid gap-4 text-sm text-muted-foreground">
                    <Link href="/settings/profile" className={`font-semibold ${pathname === "/settings/profile" ? "text-primary" : ""}`}>
                        Profile
                    </Link>
                    <Link href="#" className={`font-semibold ${pathname === "#" ? "text-primary" : ""}`}>
                        Degree
                    </Link>
                    <Link href="#" className={`font-semibold ${pathname === "#" ? "text-primary" : ""}`}>
                        Semester
                    </Link>
                    <Link href="/settings/modules" className={`font-semibold ${pathname === "/settings/modules" ? "text-primary" : ""}`}>
                        Modules
                    </Link>
                </nav>
                <div className="grid gap-6">{children}</div>
            </div>
        </main>
    );
};

export default SettingsLayout;