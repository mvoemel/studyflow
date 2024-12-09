import { tuam } from "@/lib/tuam";

type AverageGradeResponseData = {
  average: number;
};

const getAverageGrade = async (degreeId: string) => {
  const response = await tuam.get<AverageGradeResponseData>(
    `/api/degrees/${degreeId}/grades/average`
  );

  return {
    average: response.average,
  } as AverageGradeResponseData;
};

export { type AverageGradeResponseData, getAverageGrade };
