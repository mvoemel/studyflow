import { tuam } from "../tuam";
import { Degree, Settings, User } from "@/types";

type LoginRequestBody = {
  email: string;
  password: string;
};
const loginRequest = async (body: LoginRequestBody) => {
  return await tuam.post<void, LoginRequestBody>("/api/auth/login", body);
};

type RegisterRequestBody = Omit<User, "id" | "settingsId">;
const registerRequest = async (body: RegisterRequestBody) => {
  await tuam.post<void, RegisterRequestBody>("/api/auth/register", body);
};

type MeRequestResponseData = {
  user: User;
  settings: Settings;
};
const meRequest = async () => {
  return await tuam.get<MeRequestResponseData>("/api/auth/me");
};

type UpdateUserRequestBody = Omit<User, "id" | "settingsId" | "password"> & {
  password?: User["password"];
};
const updateUserRequest = async (body: UpdateUserRequestBody) => {
  return await tuam.patch<void, UpdateUserRequestBody>("/api/auth", body);
};

type UpdateActiveDegreeRequestBody = {
  activeDegreeId: Degree["id"];
};
const updateActiveDegreeRequest = async (
  body: UpdateActiveDegreeRequestBody
) => {
  return await tuam.patch<void, UpdateActiveDegreeRequestBody>(
    "/api/settings",
    body
  );
};

export {
  loginRequest,
  registerRequest,
  meRequest,
  updateUserRequest,
  updateActiveDegreeRequest,
};
