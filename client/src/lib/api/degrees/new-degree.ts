import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type NewDegreeRequestBody = Pick<Degree, "name" | "description">;

// TODO: fix type
const newDegreeRequest = async (body: NewDegreeRequestBody) => {
  const response = await tuam.post<Degree, NewDegreeRequestBody>(
    "/api/degrees",
    body
  );
  return { ...response, id: response.id.toString() };
};

export { type NewDegreeRequestBody, newDegreeRequest };
