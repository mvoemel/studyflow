import { AverageGradeResponseData, getAverageGrade } from "@/lib/api";
import useSWR from "swr";

/**
 * This hook returns the average grade for a given degreeId.
 * If degreeId is not specified no request is made, until degreeId is specified.
 *
 * @param degreeId the degree id for which the average grade is to be returned
 * @returns average grade for a given degree
 */
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
