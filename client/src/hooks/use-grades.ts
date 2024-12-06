import {
  getGradesForDegreeRequest,
  GradesForDegreeResponseData,
  updateGradesForModuleRequest,
  UpdateGradesForModuleRequestBody,
} from "@/lib/api";
import { Degree } from "@/types";
import useSWR from "swr";

const useGrades = (degreeId: string | undefined) => {
  const { data, error, isLoading, mutate } =
    useSWR<GradesForDegreeResponseData>(
      !degreeId ? null : `grades-${degreeId}`,
      () => getGradesForDegreeRequest(degreeId!)
    );

  // TODO: optimize so that data is not just revalidated but optimistically set
  const updateGrades = async (
    body: UpdateGradesForModuleRequestBody,
    degreeId: Degree["id"]
  ) => {
    await updateGradesForModuleRequest(body, degreeId);
    await mutate();
  };

  return {
    gradesTree: data,
    isLoading,
    error,
    updateGrades,
  };
};

export { useGrades };
