import { tuam } from "@/lib/tuam";

type LoginRequestBody = {
  email: string;
  password: string;
};

const loginRequest = async (body: LoginRequestBody) => {
  return await tuam.post<void, LoginRequestBody>("/api/student/login", body);
};

export { type LoginRequestBody, loginRequest };
