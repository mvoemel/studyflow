import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";
import clsx from "clsx";

type GradeBoxProps = {
  grade: number;
  description: string;
  className?: string;
};

const GradeBox = ({ grade, description, className }: GradeBoxProps) => {
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
          {grade.toFixed(1) || 0}
        </CardTitle>
      </CardContent>
    </Card>
  );
};

export { GradeBox };
