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
    () => meRequest()
  );

  const updateUser = async (body: UpdateUserRequestBody) => {
    await mutate(
      async () => {
        await updateUserRequest(body);
        return data
          ? {
              settings: data.settings,
              user: {
                ...data.user,
                ...body,
              },
            }
          : undefined;
      },
      {
        optimisticData: data
          ? {
              settings: data.settings,
              user: {
                ...data.user,
                ...body,
              },
            }
          : undefined,
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const updateActiveDegree = async (body: UpdateActiveDegreeRequestBody) => {
    await mutate(
      async () => {
        await updateActiveDegreeRequest(body);
        return data
          ? {
              settings: {
                ...data.settings,
                activeDegreeId: body.activeDegreeId,
              },
              user: data.user,
            }
          : undefined;
      },
      {
        optimisticData: data
          ? {
              settings: {
                ...data.settings,
                activeDegreeId: body.activeDegreeId,
              },
              user: data.user,
            }
          : undefined,
        rollbackOnError: false,
        populateCache: true,
        revalidate: false,
      }
    );
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