import { tuam } from "@/lib/tuam";
import { User } from "@/types";

type UpdateUserRequestBody = Omit<User, "id" | "settingsId" | "password"> & {
  password?: User["password"];
};

const updateUserRequest = async (
  userId: string,
  body: UpdateUserRequestBody
) => {
  return await tuam.post<void, UpdateUserRequestBody>(
    `/api/student/${userId}`,
    body
  );
};

export { type UpdateUserRequestBody, updateUserRequest };
