import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type AllDegreesResponseData = Degree[];

const getAllDegreesRequest = async () => {
  const response = await tuam.get<
    {
      id: number;
      name: string;
      ownerId: number;
      activeSemesterId?: number;
      description?: string;
    }[]
  >("/api/degrees");

  const allDegreesResponseData: AllDegreesResponseData = response.map((d) => ({
    id: d.id.toString(),
    name: d.name,
    userId: d.ownerId?.toString(),
    activeSemesterId:
      d.activeSemesterId === -1 ? undefined : d.activeSemesterId?.toString(),
    description: d.description,
  }));

  return allDegreesResponseData;
};

export { type AllDegreesResponseData, getAllDegreesRequest };
