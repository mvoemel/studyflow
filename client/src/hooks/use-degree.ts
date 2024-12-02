import {
  AllDegreesResponseData,
  deleteDegreeRequest,
  getAllDegreesRequest,
  newDegreeRequest,
  NewDegreeRequestBody,
  updateDegreeRequest,
  UpdateDegreeRequestBody,
} from "@/lib/api";
import { Degree } from "@/types";
import useSWR from "swr";

const useDegrees = () => {
  const { data, error, mutate, isLoading } = useSWR<AllDegreesResponseData>(
    "degrees",
    () => getAllDegreesRequest()
  );

  const addNewDegree = async (body: NewDegreeRequestBody) => {
    const optimisticDegree = {
      ...body,
      id: `tmp-degree-id-${Date.now()}`,
      userId: `tmp-user-id-${Date.now()}`,
    };

    await mutate(
      async () => {
        const addedDegree = await newDegreeRequest(body);
        return [...(data || []), addedDegree];
      },
      {
        optimisticData: [...(data || []), optimisticDegree],
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const updateDegree = async (
    body: UpdateDegreeRequestBody,
    degreeId: Degree["id"]
  ) => {
    await mutate(
      async () => {
        await updateDegreeRequest(body, degreeId);
        return (data || []).map((d) =>
          d.id === degreeId ? { ...d, ...body } : d
        );
      },
      {
        optimisticData: (data || []).map((d) =>
          d.id === degreeId ? { ...d, ...body } : d
        ),
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const deleteDegree = async (degreeId: Degree["id"]) => {
    await mutate(
      async () => {
        await deleteDegreeRequest(degreeId);
        return (data || []).filter((d) => d.id !== degreeId);
      },
      {
        optimisticData: (data || []).filter((d) => d.id !== degreeId),
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  return {
    degrees: data,
    isLoading,
    error,
    addNewDegree,
    updateDegree,
    deleteDegree,
  };
};

export { useDegrees };
