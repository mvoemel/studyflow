import { dashboardRequest, DashboardResponseData } from "@/lib/api";
import useSWR from "swr";

const useEctsAverage = () => {
  const { data, error, isLoading } = useSWR<DashboardResponseData>(
    "ects-average",
    dashboardRequest
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
