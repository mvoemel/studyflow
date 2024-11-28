import { Settings } from "./settings";
import { User } from "./user";

export type TokenPayload = {
  userId: User["id"];
  email: User["email"];
  settingsId: Settings["id"];
  iat: number;
  exp: number;
};

export type TokenAttributes = Omit<TokenPayload, "iat" | "exp">;
