import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type NewDegreeRequestBody = Pick<Degree, "name" | "description">;

const newDegreeRequest = async (body: NewDegreeRequestBody) => {
  return await tuam.post<void, NewDegreeRequestBody>("/api/degrees", body);
};

export { type NewDegreeRequestBody, newDegreeRequest };
