"use client";

import { LoadingSpinner } from "@/components/global/loading-spinner";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/forms";
import { Input } from "@/components/ui/input";
import { useUserSettings } from "@/hooks/use-user-settings";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { z } from "zod";
import {useDegrees} from "@/hooks/use-degree";
import {useParams} from "next/navigation";
import {awaitTimeout} from "@/app/api-old/_utils";
import {deleteDegreeRequest} from "@/lib/api";

const degreePanelSchema = z.object({
  name: z.string().min(2, "Name must be at least 2 characters long"),
  description: z.string(),
});

const DegreePanel = () => {



  const { user, settings } = useUserSettings();
  const { degrees, updateDegree, deleteDegree } = useDegrees();

  const handleDeleteDegree = async() => {
      if(!currDegree?.id) return;

      try {
          await deleteDegree(currDegree?.id);
            toast.success("Successfully deleted degree!");
      } catch (err) {
          toast.error("Failed to delete degree!");
      }
  }

  const currDegree = degrees?.find((degree) => degree.id === settings?.activeDegreeId)

  const form = useForm<z.infer<typeof degreePanelSchema>>({
    resolver: zodResolver(degreePanelSchema),
    defaultValues: {
      name: currDegree?.name,
      description: currDegree?.description,
    },
  });

  const onSubmit = async (values: z.infer<typeof degreePanelSchema>) => {
    if(currDegree?.id === undefined) return;
      try {
      const body = {...values, id: currDegree?.id};
      await updateDegree(body, currDegree?.id);

      toast.success("Successfully updated profile!");
    } catch (err) {
      toast.error("Failed to update profile!");
    }
  };

  if (!user) {
    return (
        <div className="flex flex-col items-center justify-center">
          <LoadingSpinner />
        </div>
    );
  }

  return (
      <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <Card className="bg-muted/50">
              <CardHeader>
                <CardTitle>Your Degree</CardTitle>
                <CardDescription>
                  Here you can view your currently active degree and update it.
                </CardDescription>
              </CardHeader>
              <CardContent className="grid md:grid-cols-2 gap-4">
                <FormField
                    control={form.control}
                    name="name"
                    render={({ field }) => (
                        <FormItem>
                          <FormLabel>Name</FormLabel>
                          <FormControl>
                            <Input placeholder="Name" {...field} />
                          </FormControl>
                          <FormDescription>Enter a name</FormDescription>
                          <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="description"
                    render={({ field }) => (
                        <FormItem>
                          <FormLabel>Description</FormLabel>
                          <FormControl>
                            <Input placeholder="Description" {...field} />
                          </FormControl>
                          <FormDescription>Enter the description.</FormDescription>
                          <FormMessage />
                        </FormItem>
                    )}
                />
              </CardContent>
              <CardFooter className="border-t px-6 py-4 flex justify-end gap-4">
                  <Button variant="destructive" onClick={(event) => {
                      event.preventDefault();
                      handleDeleteDegree();
                  }} >Delete</Button>
                  <Button type="submit">Save</Button>
              </CardFooter>
            </Card>
          </form>
        </Form>
      </div>
  );
};

export default DegreePanel;
