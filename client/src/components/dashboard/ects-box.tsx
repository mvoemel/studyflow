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

type EctsBoxProps = {
  ectsPoints: number;
  maxEctsPoints: number;
  className?: string;
};

const EctsBox = ({ ectsPoints, maxEctsPoints, className }: EctsBoxProps) => {
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

export { EctsBox };
