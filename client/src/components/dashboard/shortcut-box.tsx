import { Button } from "@/components/ui/button";
import {
  Card,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { cn } from "@/lib/utils";
import Link from "next/link";

type ShortCutBoxProps = {
  title: string;
  buttonLabel: string;
  buttonLink: string;
  description?: string;
  className?: string;
};

const ShortCutBox = ({
  title,
  buttonLabel,
  buttonLink,
  description,
  className,
}: ShortCutBoxProps) => {
  return (
    <Card className={cn(className, "bg-muted/50")}>
      <CardHeader className="pb-3">
        <CardTitle>{title}</CardTitle>
        {description && (
          <CardDescription className="max-w-lg text-balance leading-relaxed">
            {description}
          </CardDescription>
        )}
      </CardHeader>
      <CardFooter>
        <Button asChild>
          <Link href={buttonLink}>{buttonLabel}</Link>
        </Button>
      </CardFooter>
    </Card>
  );
};

export { ShortCutBox };
