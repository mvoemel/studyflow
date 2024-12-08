import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";
import clsx from "clsx";
import { Skeleton } from "../ui/skeleton";

type GradeBoxProps = {
  grade: number | undefined;
  description: string;
  className?: string;
};

const GradeBox = ({ grade, description, className }: GradeBoxProps) => {
  if (grade === undefined) return <GradeBoxSkeleton className={className} />;

  return (
    <Card
      className={cn(className, "flex flex-col justify-between bg-muted/50")}
    >
      <CardHeader>
        <CardDescription>{description}</CardDescription>
      </CardHeader>
      <CardContent>
        <CardTitle
          className={clsx("text-5xl", {
            "text-green-500": grade >= 5,
            "text-yellow-500": grade >= 4.5 && grade < 5,
            "text-orange-500": grade >= 4 && grade < 4.5,
            "text-red-500": grade < 4,
          })}
        >
          {Number.isNaN(grade) ? 0 : grade /*?.toFixed(1)*/}
        </CardTitle>
      </CardContent>
    </Card>
  );
};

const GradeBoxSkeleton = ({ className }: Pick<GradeBoxProps, "className">) => {
  return (
    <Card
      className={cn(className, "flex flex-col justify-between bg-muted/50 p-4")}
    >
      <Skeleton className="h-5 w-[100px]" />
      <Skeleton className="h-12 w-16 rounded-md" />
    </Card>
  );
};

export { GradeBox };
