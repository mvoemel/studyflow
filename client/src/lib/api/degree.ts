import { Degree } from "@/types";
import { tuam } from "../tuam";

type NewDegreeRequestBody = Pick<Degree, "name" | "description">;
const newDegreeRequest = async (body: NewDegreeRequestBody) => {
  return await tuam.post<void, NewDegreeRequestBody>("/api/degrees", body);
};

type AllDegreesResponseData = Degree[];
const getAllDegrees = async () => {
  return await tuam.get<AllDegreesResponseData>("/api/degrees");
};

type UpdateDegreeRequestBody = Pick<
  Degree,
  "name" | "description" | "activeSemesterId"
>;
const updateDegreeRequest = async (
  body: UpdateDegreeRequestBody,
  degreeId: string
) => {
  return await tuam.patch<void, UpdateDegreeRequestBody>(
    `/api/degrees/${degreeId}`,
    body
  );
};

export { newDegreeRequest, getAllDegrees, updateDegreeRequest };
