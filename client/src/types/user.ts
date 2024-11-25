import { Settings } from "./settings";

export type User = {
  id: string;
  firstname: string;
  lastname: string;
  email: string;
  settingsId: Settings["id"];
};
