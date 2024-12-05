import { tuam } from "@/lib/tuam";

const logoutRequest = async () => {
  return await tuam.post<void, {}>("/api/student/logout", {});
};

export { logoutRequest };
