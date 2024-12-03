import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type AllDegreesResponseData = Degree[];

// TODO: fix type
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

  return response.map((d) => ({
    id: d.id.toString(),
    name: d.name,
    userId: d.userId?.toString(),
    activeSemesterId: d.activeSemesterId?.toString(),
    description: d.description,
  }));
};

export { type AllDegreesResponseData, getAllDegreesRequest };
