import { tuam } from "@/lib/tuam";
import { Settings } from "@/types";

type UpdateActiveDegreeRequestBody = Settings;

const updateActiveDegreeRequest = async (
  settingsId: string,
  body: UpdateActiveDegreeRequestBody
) => {
  const newBody = {
    id: body.id,
    activeDegree: body.activeDegreeId || "-1",
    globalCalendar: body.globalCalendarId,
  };

  return await tuam.post<
    void,
    { id: string; activeDegree: string; globalCalendar: string }
  >(`/api/student/settings/${settingsId}`, newBody);
};

export { type UpdateActiveDegreeRequestBody, updateActiveDegreeRequest };
