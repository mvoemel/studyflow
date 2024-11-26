import { Settings } from "./settings";

export type User = {
  id: string;
  firstname: string;
  lastname: string;
  email: string;
  password: string;
  settingsId: Settings["id"];
};

export type UserWithoutPassword = Omit<UserActivation, "password">;
