import { tuam } from "@/lib/tuam";
import { Settings, User } from "@/types";

type MeRequestResponseData = {
  user: User;
  settings: Settings;
};

const meRequest = async () => {
  return await tuam.get<MeRequestResponseData>("/api/auth/me");
};

export { type MeRequestResponseData, meRequest };
