import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export const cn = (...inputs: ClassValue[]) => {
  return twMerge(clsx(inputs));
};

export const adjustToLocalTime = (date: Date): Date => {
  const localOffsetMinutes = date.getTimezoneOffset();

  const adjustedDate = new Date(
    date.getTime() - localOffsetMinutes * 60 * 1000
  );

  return adjustedDate;
};
