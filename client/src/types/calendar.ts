import { User } from "./user";

export type Calendar = {
  id: string;
  type: "global" | "semester";
  userId: User["id"];
};
