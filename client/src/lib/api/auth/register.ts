import { tuam } from "@/lib/tuam";
import { User } from "@/types";

type RegisterRequestBody = Omit<User, "id" | "settingsId">;

const registerRequest = async (body: RegisterRequestBody) => {
  await tuam.post<void, RegisterRequestBody>("/api/auth/register", body);
};

export { type RegisterRequestBody, registerRequest };
