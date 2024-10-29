import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";

type ActiveModuleBoxProps = {
  className?: string;
};

// TODO: implement
const ActiveModuleBox = ({ className }: ActiveModuleBoxProps) => {
  return (
    <Card className={cn(className)}>
      <CardHeader className="pb-3">
        <CardTitle>Placeholder</CardTitle>
        <CardDescription className="max-w-lg text-balance leading-relaxed">
          Placeholder <br /> Placeholder <br /> Placeholder <br /> Placeholder
        </CardDescription>
      </CardHeader>
    </Card>
  );
};

export { ActiveModuleBox };
