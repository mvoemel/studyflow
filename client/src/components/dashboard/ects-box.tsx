import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Progress } from "@/components/ui/progress";
import { cn } from "@/lib/utils";
import { Skeleton } from "../ui/skeleton";

type EctsBoxProps = {
  ectsPoints: number | undefined;
  maxEctsPoints: number | undefined;
  className?: string;
};

const EctsBox = ({ ectsPoints, maxEctsPoints, className }: EctsBoxProps) => {
  if (ectsPoints === undefined || maxEctsPoints === undefined)
    return <EctsBoxSkeleton className={className} />;

  return (
    <Card
      className={cn(className, "flex flex-col justify-between bg-muted/50")}
    >
      <CardHeader className="pb-2">
        <CardDescription>ECTS Points</CardDescription>
        <CardTitle className="text-4xl">{ectsPoints}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="text-xs text-muted-foreground">
          out of {maxEctsPoints} ECTS Points
        </div>
      </CardContent>
      <CardFooter>
        <Progress value={(ectsPoints / maxEctsPoints) * 100} />
      </CardFooter>
    </Card>
  );
};

const EctsBoxSkeleton = ({ className }: Pick<EctsBoxProps, "className">) => {
  return (
    <Card
      className={cn(className, "flex flex-col justify-between bg-muted/50 p-4")}
    >
      <div className="space-y-2">
        <Skeleton className="h-5 w-[150px]" />
        <Skeleton className="h-12 w-16 rounded-md" />
      </div>
      <Skeleton className="h-3 w-2/3" />
      <Skeleton className="h-5 w-full" />
    </Card>
  );
};

export { EctsBox };
