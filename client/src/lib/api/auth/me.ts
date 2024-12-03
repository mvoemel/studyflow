import { tuam } from "@/lib/tuam";
import { Settings, UserWithoutPassword } from "@/types";

type MeRequestResponseData = {
  user: UserWithoutPassword;
  settings: Settings;
};

const meRequest = async () => {
  const response = await tuam.get<{
    student: UserWithoutPassword;
    settings: Settings;
  }>("/api/student/me");
  return { user: response.student, settings: response.settings };
};

export { type MeRequestResponseData, meRequest };
