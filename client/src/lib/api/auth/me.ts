import { tuam } from "@/lib/tuam";
import { Settings, UserWithoutPassword } from "@/types";

type MeRequestResponseData = {
  user: UserWithoutPassword;
  settings: Settings;
};

const meRequest = async () => {
  const response = await tuam.get<{
    student: UserWithoutPassword;
    settings: { id: number; activeDegree: number; globalCalendar: number };
  }>("/api/student/me");

  const meResponseData: MeRequestResponseData = {
    user: {
      ...response.student,
      id: response.student.id.toString(),
      settingsId: response.settings.id.toString(),
    },
    settings: {
      id: response.settings.id.toString(),
      activeDegreeId: response.settings.activeDegree.toString(),
      globalCalendarId: response.settings.globalCalendar.toString(),
    },
  };

  return meResponseData;
};

export { type MeRequestResponseData, meRequest };
