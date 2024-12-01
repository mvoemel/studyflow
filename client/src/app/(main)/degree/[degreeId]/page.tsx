"use client";

import { useParams } from "next/navigation";

// TODO: implement page where current degree can be updated and deleted

const DegreePage = () => {
  const { degreeId } = useParams();

  return <div>DegreePage: {degreeId}</div>;
};

export default DegreePage;
