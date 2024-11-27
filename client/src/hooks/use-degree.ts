import {
  AllDegreesResponseData,
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
    getAllDegreesRequest
  );

  const addNewDegree = async (body: NewDegreeRequestBody) => {
    await newDegreeRequest(body);
    mutate(); // TODO: change so new request is not sent
  };

  const updateDegree = async (
    body: UpdateDegreeRequestBody,
    degreeId: Degree["id"]
  ) => {
    await updateDegreeRequest(body, degreeId);
    mutate(); // TODO: change so new request is not sent
  };

  //   const deleteDegree = async (degreeId: Degree["id"]) => {
  //     await deleteDegreeRequest(degreeId);
  //     mutate(); // TODO: change so new request is not sent
  //   };

  return {
    degrees: data,
    isLoading,
    error,
    addNewDegree,
    updateDegree,
    // deleteDegree,
  };
};

export { useDegrees };
