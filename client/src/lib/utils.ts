import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export const cn = (...inputs: ClassValue[]) => {
  return twMerge(clsx(inputs));
};

export const adjustToLocalTime = (date: Date): Date => {
  // Get the local timezone offset in minutes
  const localOffsetMinutes = date.getTimezoneOffset();
  console.log("offset: " + localOffsetMinutes);

  // Adjust the date by the timezone offset (convert minutes to milliseconds)
  const adjustedDate = new Date(
    date.getTime() - localOffsetMinutes * 60 * 1000
  );

  return adjustedDate;
};
