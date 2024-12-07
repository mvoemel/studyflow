import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type NewDegreeRequestBody = Pick<Degree, "name" | "description">;

const newDegreeRequest = async (body: NewDegreeRequestBody) => {
  const response = await tuam.post<
    {
      id: number;
      name: string;
      userId: number;
      activeSemesterId?: number;
      description?: string;
    },
    NewDegreeRequestBody
  >("/api/degrees", body);

  const activeSemesterId =
    response.activeSemesterId === -1 ? undefined : response.activeSemesterId;

  const newDegreeResponseData: Degree = {
    ...response,
    id: response.id.toString(),
    userId: response.userId?.toString(),
    activeSemesterId: activeSemesterId?.toString(),
  };

  return newDegreeResponseData;
};

export { type NewDegreeRequestBody, newDegreeRequest };
