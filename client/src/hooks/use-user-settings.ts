import {
  meRequest,
  MeRequestResponseData,
  updateActiveDegreeRequest,
  UpdateActiveDegreeRequestBody,
  updateUserRequest,
  UpdateUserRequestBody,
} from "@/lib/api";
import useSWR from "swr";

const useUserSettings = () => {
  const { data, error, mutate, isLoading } = useSWR<MeRequestResponseData>(
    "user-settings",
    meRequest
  );

  const updateUser = async (body: UpdateUserRequestBody) => {
    await updateUserRequest(body);
    mutate(); // TODO: change so new request is not sent
  };

  const updateActiveDegree = async (body: UpdateActiveDegreeRequestBody) => {
    await updateActiveDegreeRequest(body);
    mutate(); // TODO: change so new request is not sent
  };

  return {
    user: data?.user,
    settings: data?.settings,
    isLoading,
    error,
    updateUser,
    updateActiveDegree,
  };
};

export { useUserSettings };
