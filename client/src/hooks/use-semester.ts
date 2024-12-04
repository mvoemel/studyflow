import { awaitTimeout } from "@/app/api-old/_utils";
import {
  AllSemestersResponseData,
  deleteSemesterRequest,
  getAllSemestersRequest,
  newSemesterRequest,
  NewSemesterRequestBody,
  updateSemesterRequest,
  UpdateSemesterRequestBody,
} from "@/lib/api";
import { Semester } from "@/types";
import useSWR from "swr";

const useSemesters = () => {
  const { data, error, mutate, isLoading } = useSWR<AllSemestersResponseData>(
    "semesters",
    () => getAllSemestersRequest()
  );

  const addNewSemester = async (body: NewSemesterRequestBody) => {
    const optimisticSemester = {
      ...body,
      id: `tmp-semester-id-${Date.now()}`,
      userId: `tmp-user-id-${Date.now()}`,
    };

    await mutate(
      async () => {
        const addedSemester = await newSemesterRequest(body);
        return [...(data || []), addedSemester];
      },
      {
        optimisticData: [...(data || []), optimisticSemester],
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const updateSemester = async (
    body: UpdateSemesterRequestBody,
    semesterId: Semester["id"]
  ) => {
    await mutate(
      async () => {
        await updateSemesterRequest(body, semesterId);
        return (data || []).map((d) =>
          d.id === semesterId ? { ...d, ...body } : d
        );
      },
      {
        optimisticData: (data || []).map((d) =>
          d.id === semesterId ? { ...d, ...body } : d
        ),
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const deleteSemester = async (semesterId: Semester["id"]) => {
    await mutate(
      async () => {
        await deleteSemesterRequest(semesterId);
        return (data || []).filter((d) => d.id !== semesterId);
      },
      {
        optimisticData: (data || []).filter((d) => d.id !== semesterId),
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  return {
    semesters: data,
    isLoading,
    error,
    addNewSemester,
    updateSemester,
    deleteSemester,
  };
};

export { useSemesters };
