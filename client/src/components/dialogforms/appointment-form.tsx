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
import { useEffect } from "react";

const appointmentFormSchema = z
  .object({
    title: z.string().min(1, "Title is required"),
    description: z.string().optional(),
    startDateTime: z.string().min(1, "Start date is required"),
    endDateTime: z.string().min(1, "End date is required"),
  })
  .refine((date) => date.startDateTime < date.endDateTime, {
    message: "Start date must be before end date",
    path: ["endDateTime"],
  });

export type AppointmentFormValues = z.infer<typeof appointmentFormSchema>;

type AppointmentFormProps = {
  onSubmit: (data: AppointmentFormValues) => void;
  onDelete: () => void;
  initialData?: AppointmentFormValues | null;
};

const AppointmentForm = ({
  onSubmit,
  onDelete,
  initialData,
}: AppointmentFormProps) => {
  const form = useForm<z.infer<typeof appointmentFormSchema>>({
    resolver: zodResolver(appointmentFormSchema),
    defaultValues: initialData || {
      title: "",
      description: "",
      startDateTime: "",
      endDateTime: "",
    },
  });

  useEffect(() => {
    if (initialData) {
      Object.entries(initialData).forEach(([key, value]) =>
        form.setValue(key as keyof AppointmentFormValues, value)
      );
    }
  }, [initialData, form]);

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
        <FormField
          control={form.control}
          name="title"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Appointment Title</FormLabel>
              <FormControl>
                <Input placeholder="Appointment Title" {...field} />
              </FormControl>
              <FormDescription>
                Enter the title of the appointment.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="description"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Appointment Description</FormLabel>
              <FormControl>
                <Textarea placeholder="Appointment Description" {...field} />
              </FormControl>
              <FormDescription>
                Enter a brief description of the appointment. (optional)
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="startDateTime"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Appointment Start Date</FormLabel>
              <FormControl>
                <Input type="datetime-local" {...field} />
              </FormControl>
              <FormDescription>
                Enter the start date of the appointment.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="endDateTime"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Appointment End Date</FormLabel>
              <FormControl>
                <Input type="datetime-local" {...field} />
              </FormControl>
              <FormDescription>
                Enter the end date of the appointment.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button type="submit">Submit</Button>
        {!!initialData && (
          <Button variant="destructive" onClick={onDelete}>
            Delete
          </Button>
        )}
      </form>
    </Form>
  );
};

export { AppointmentForm };
