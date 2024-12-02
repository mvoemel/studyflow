import {
  getGradesForDegreeRequest,
  GradesForDegreeResponseData,
} from "@/lib/api";
import useSWR from "swr";

const useGrades = (degreeId: string | undefined) => {
  const { data, error, isLoading } = useSWR<GradesForDegreeResponseData>(
    !degreeId ? null : `grades-${degreeId}`,
    () => getGradesForDegreeRequest(degreeId!)
  );

  return {
    gradesTree: data,
    isLoading,
    error,
  };
};

export { useGrades };
