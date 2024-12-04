import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type AllDegreesResponseData = Degree[];

const getAllDegreesRequest = async () => {
  const response = await tuam.get<
    {
      id: number;
      name: string;
      userId: number;
      activeSemesterId?: number;
      description?: string;
    }[]
  >("/api/degrees");

  const allDegreesResponseData: AllDegreesResponseData = response.map((d) => ({
    id: d.id.toString(),
    name: d.name,
    userId: d.userId?.toString(),
    activeSemesterId: d.activeSemesterId?.toString(),
    description: d.description,
  }));

  return allDegreesResponseData;
};

export { type AllDegreesResponseData, getAllDegreesRequest };
