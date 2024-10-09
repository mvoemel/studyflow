"use client";

import { useEffect, useState } from "react";
import { usePathname } from "next/navigation";

/**
 * This hook returns the current base path.
 *
 * Example: "/settings" from "/settings/profile".
 *
 * @returns basePath: The base path.
 */
const useBasePath = () => {
  const pathname = usePathname();
  const [basePath, setBasePath] = useState<string>();

  useEffect(() => {
    setBasePath(`/${pathname.split("/")[1]}`);
  }, [pathname]);

  return basePath;
};

export { useBasePath };
