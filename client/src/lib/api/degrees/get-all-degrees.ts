import { tuam } from "@/lib/tuam";
import { Degree } from "@/types";

type AllDegreesResponseData = Degree[];

const getAllDegreesRequest = async () => {
  return await tuam.get<AllDegreesResponseData>("/api/degrees");
};

export { type AllDegreesResponseData, getAllDegreesRequest };
