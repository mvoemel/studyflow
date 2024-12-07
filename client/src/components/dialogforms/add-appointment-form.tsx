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
import { useAppointments } from "@/hooks/use-appointments";
import { useUserSettings } from "@/hooks/use-user-settings";
import { toast } from "sonner";

const formsSchema = z.object({
  appointmentTitle: z
    .string()
    .min(2, "Degree Title must be at least 2 characters long")
    .max(50),
  appointmentDescription: z
    .string()
    .max(200, "Degree description must be at most 200 characters long"),
  appointmentStartDate: z.string(),
  appointmentEndDate: z.string(),
});

type AddApointmentFormsProps = {
  defaultValues?: Partial<z.infer<typeof formsSchema>>;
  onClose: () => void;
};

export function AddAppointmentForm({
  defaultValues,
  onClose,
}: AddApointmentFormsProps) {
  const { settings } = useUserSettings();
  const { addNewAppointment } = useAppointments(settings?.globalCalendarId);

  const form = useForm<z.infer<typeof formsSchema>>({
    resolver: zodResolver(formsSchema),
    defaultValues: defaultValues || {
      appointmentTitle: "",
      appointmentDescription: "",
      appointmentStartDate: new Date().toISOString(),
      appointmentEndDate: new Date().toISOString(),
    },
  });

  const onSubmit = async (values: z.infer<typeof formsSchema>) => {
    onClose();

    try {
      await addNewAppointment({
        title: values.appointmentTitle,
        description: values.appointmentDescription,
        startDateTime: new Date(values.appointmentStartDate),
        endDateTime: new Date(values.appointmentEndDate),
      });

      toast.success("Successfully added new appointment!");
    } catch (err) {
      toast.error("Failed to add new appointment.");
    }
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
        <FormField
          control={form.control}
          name="appointmentTitle"
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
          name="appointmentDescription"
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
          name="appointmentStartDate"
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
          name="appointmentEndDate"
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
        <Button type="submit">Add</Button>
        <Button variant="ghost" onClick={onClose}>
          Cancel
        </Button>
      </form>
    </Form>
  );
}
