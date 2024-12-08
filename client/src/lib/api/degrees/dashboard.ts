import { tuam } from "@/lib/tuam";

type DashboardResponseData = {
  averageDegreeGrade: number;
  currEcts: number;
  totalEcts: number;
};

const dashboardRequest = async (degreeId: string) => {
  const [averageResponse, averageGradesResponse] = await Promise.all([
    tuam.get<Pick<DashboardResponseData, "averageDegreeGrade">>(
      `/api/degrees/${degreeId}/grades/average`
    ),
    tuam.get<Pick<DashboardResponseData, "currEcts" | "totalEcts">>(
      `/api/degrees/${degreeId}/ects`
    ),
  ]);

  // TODO: parse data
  // `/api/degrees/${degreeId}/grades/average` returns {average: number}

  return {
    ...averageResponse,
    ...averageGradesResponse,
  } as DashboardResponseData;
};

export { type DashboardResponseData, dashboardRequest };
