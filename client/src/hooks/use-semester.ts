import {
  AllSemestersResponseData,
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
    getAllSemestersRequest
  );

  const addNewSemester = async (body: NewSemesterRequestBody) => {
    await newSemesterRequest(body);
    mutate(); // TODO: change so new request is not sent
  };

  const updateSemester = async (
    body: UpdateSemesterRequestBody,
    semesterId: Semester["id"]
  ) => {
    await updateSemesterRequest(body, semesterId);
    mutate(); // TODO: change so new request is not sent
  };

  //   const deleteSemester = async (semesterId: Semester["id"]) => {
  //     await deleteSemesterRequest(semesterId);
  //     mutate(); // TODO: change so new request is not sent
  //   };

  return {
    semesters: data,
    isLoading,
    error,
    addNewSemester,
    updateSemester,
    // deleteSemester,
  };
};

export { useSemesters };
