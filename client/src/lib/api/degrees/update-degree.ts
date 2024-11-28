import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

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

export { type UpdateDegreeRequestBody, updateDegreeRequest };
