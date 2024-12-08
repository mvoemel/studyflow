import { AverageGradeResponseData, getAverageGrade } from "@/lib/api";
import useSWR from "swr";

const useAverage = (degreeId: string | undefined) => {
  const { data, error, isLoading } = useSWR<AverageGradeResponseData>(
    !degreeId ? null : `average-${degreeId}`,
    () => getAverageGrade(degreeId!)
  );

  return {
    average: data?.average,
    isLoading,
    error,
  };
};

export { useAverage };
