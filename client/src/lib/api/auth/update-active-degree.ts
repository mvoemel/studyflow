import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type UpdateActiveDegreeRequestBody = {
  activeDegreeId: Degree["id"];
};

// TODO: fix type
const updateActiveDegreeRequest = async (
  settingsId: string,
  body: UpdateActiveDegreeRequestBody
) => {
  const newBody = { activeDegree: body.activeDegreeId };

  return await tuam.patch<void, { activeDegree: string }>(
    `/api/student/settings/${settingsId}`,
    newBody
  );
};

export { type UpdateActiveDegreeRequestBody, updateActiveDegreeRequest };
