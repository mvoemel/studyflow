import {
  meRequest,
  MeRequestResponseData,
  updateActiveDegreeRequest,
  UpdateActiveDegreeRequestBody,
  updateUserRequest,
  UpdateUserRequestBody,
} from "@/lib/api";
import useSWR from "swr";

/**
 * This hook holds the user and settings object of the current user.
 *
 * @returns an user object (without password) and a settings object
 */
const useUserSettings = () => {
  const { data, error, mutate, isLoading } = useSWR<MeRequestResponseData>(
    "user-settings",
    () => meRequest()
  );

  const updateUser = async (body: UpdateUserRequestBody) => {
    if (!data?.user) return;

    await mutate(
      async () => {
        await updateUserRequest(data?.user.id, body);
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
    if (!data?.settings) return;

    await mutate(
      async () => {
        await updateActiveDegreeRequest(data?.settings.id, body);
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
