import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type UpdateActiveDegreeRequestBody = {
  activeDegreeId: Degree["id"];
};

const updateActiveDegreeRequest = async (
  settingsId: string,
  body: UpdateActiveDegreeRequestBody
) => {
  const newBody = { activeDegree: body.activeDegreeId };

  return await tuam.post<void, { activeDegree: string }>(
    `/api/student/settings/${settingsId}`,
    newBody
  );
};

export { type UpdateActiveDegreeRequestBody, updateActiveDegreeRequest };
