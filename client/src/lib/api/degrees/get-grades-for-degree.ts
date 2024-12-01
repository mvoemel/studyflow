import { tuam } from "@/lib/tuam";
import { GradeViewTree } from "@/types";

type GradesForDegreeResponseData = GradeViewTree;

const getGradesForDegreeRequest = async (degreeId: string) => {
  return await tuam.get<GradesForDegreeResponseData>(
    `/api/degrees/${degreeId}/grades`
  );
};

export { type GradesForDegreeResponseData, getGradesForDegreeRequest };
