import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type UpdateActiveDegreeRequestBody = {
  activeDegreeId: Degree["id"];
};

const updateActiveDegreeRequest = async (
  body: UpdateActiveDegreeRequestBody
) => {
  return await tuam.patch<void, UpdateActiveDegreeRequestBody>(
    "/api/auth/settings",
    body
  );
};

export { type UpdateActiveDegreeRequestBody, updateActiveDegreeRequest };
