import { tuam } from "@/lib/tuam";
import { User } from "@/types";

type UpdateUserRequestBody = Omit<User, "id" | "settingsId" | "password"> & {
  password?: User["password"];
};

const updateUserRequest = async (body: UpdateUserRequestBody) => {
  return await tuam.patch<void, UpdateUserRequestBody>("/api/auth", body);
};

export { type UpdateUserRequestBody, updateUserRequest };
