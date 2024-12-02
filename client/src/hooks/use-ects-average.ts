import { dashboardRequest, DashboardResponseData } from "@/lib/api";
import useSWR from "swr";

const useEctsAverage = (degreeId: string | undefined) => {
  const { data, error, isLoading } = useSWR<DashboardResponseData>(
    !degreeId ? null : `ects-average-${degreeId}`,
    () => dashboardRequest(degreeId!)
  );

  return {
    average: data?.averageDegreeGrade,
    currEcts: data?.currEcts,
    totalEcts: data?.totalEcts,
    isLoading,
    error,
  };
};

export { useEctsAverage };
