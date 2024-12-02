"use client";

import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/forms";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useDegrees } from "@/hooks/use-degree";
import { toast } from "sonner";

const formsSchema = z.object({
  degreeTitle: z
    .string()
    .min(2, "Degree Title must be at least 2 characters long")
    .max(50),
  degreeDescription: z
    .string()
    .max(200, "Degree description must be at most 200 characters long"),
});

type AddDegreeFormsProps = {
  defaultValues?: Partial<z.infer<typeof formsSchema>>;
  onClose: () => void;
};

export function AddDegreeForms({
  defaultValues,
  onClose,
}: AddDegreeFormsProps) {
  const { addNewDegree } = useDegrees();

  const form = useForm<z.infer<typeof formsSchema>>({
    resolver: zodResolver(formsSchema),
    defaultValues: defaultValues || {
      degreeTitle: "",
      degreeDescription: "",
    },
  });

  async function onSubmit(values: z.infer<typeof formsSchema>) {
    onClose();

    try {
      await addNewDegree({
        name: values.degreeTitle,
        description: values.degreeDescription,
      });

      toast.success("Successfully added degree!");
    } catch (err) {
      toast.error("Failed to add new degree.");
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
        <FormField
          control={form.control}
          name="degreeTitle"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Degree Title</FormLabel>
              <FormControl>
                <Input placeholder="Degree Title" {...field} />
              </FormControl>
              <FormDescription>Enter the title of the Degree.</FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="degreeDescription"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Degree Description</FormLabel>
              <FormControl>
                <Textarea placeholder="Degree Description" {...field} />
              </FormControl>
              <FormDescription>
                Enter a brief description of the degree.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button type="submit">Add</Button>
        <Button variant="ghost" onClick={onClose}>
          Cancel
        </Button>
      </form>
    </Form>
  );
}
