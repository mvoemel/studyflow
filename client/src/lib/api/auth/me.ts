import { tuam } from "@/lib/tuam";
import { Settings, UserWithoutPassword } from "@/types";

type MeRequestResponseData = {
  user: UserWithoutPassword;
  settings: Settings;
};

const meRequest = async () => {
  return await tuam.get<MeRequestResponseData>("/api/auth/me");
};

export { type MeRequestResponseData, meRequest };
