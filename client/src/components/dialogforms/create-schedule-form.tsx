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
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Checkbox } from "@/components/ui/checkbox";
import {
  createStudyplanRequest,
  CreateStudyplanRequestBody,
  DayId,
  DAYS_OF_WEEK,
} from "@/lib/api";
import { mutate } from "swr";

const formsSchema = z.object({
  startDate: z.string().min(1, "Start date is required"),
  endDate: z.string().min(1, "End date is required"),
  daysOfWeek: z.array(z.string()).nonempty("At least one day is required"),
  startTime: z.string().min(1, "Start time is required"),
  endTime: z.string().min(1, "End time is required"),
});

type CreateScheduleFormsProps = {
  defaultValues?: Partial<z.infer<typeof formsSchema>>;
  onClose: () => void;
  semesterId?: string;
  settingsId?: string;
};

export function CreateScheduleForm({
  defaultValues,
  onClose,
  semesterId,
  settingsId,
}: CreateScheduleFormsProps) {
  const form = useForm<z.infer<typeof formsSchema>>({
    resolver: zodResolver(formsSchema),
    defaultValues: defaultValues || {
      startDate: "",
      endDate: "",
      daysOfWeek: [],
      startTime: "",
      endTime: "",
    },
  });

  async function onSubmit(values: z.infer<typeof formsSchema>) {
    if (!semesterId || !settingsId) return;

    onClose();

    const body: CreateStudyplanRequestBody = {
      settingsId,
      semesterId,
      startDate: new Date(values.startDate).toISOString().split("T")[0],
      endDate: new Date(values.endDate).toISOString().split("T")[0],
      daysOfWeek: values.daysOfWeek as DayId[],
      dayStartTime: values.startTime,
      dayEndTime: values.endTime,
    };

    await createStudyplanRequest(body);
    await mutate("semesters");
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
        <div className="grid grid-cols-2 gap-2">
          <FormField
            control={form.control}
            name="startDate"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Start Date</FormLabel>
                <FormControl>
                  <Input type="date" {...field} />
                </FormControl>
                <FormDescription>
                  Enter the start date of the schedule.
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="endDate"
            render={({ field }) => (
              <FormItem>
                <FormLabel>End Date</FormLabel>
                <FormControl>
                  <Input type="date" {...field} />
                </FormControl>
                <FormDescription>
                  Enter the end date of the schedule.
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <FormItem>
          <FormLabel>Days of the Week</FormLabel>
          <FormControl>
            <div className="grid">
              {DAYS_OF_WEEK.map((day) => (
                <label
                  key={day.id}
                  className="inline-flex items-center space-x-2"
                >
                  <Checkbox
                    value={day.id}
                    checked={form.watch("daysOfWeek").includes(day.id)}
                    onCheckedChange={(checked) => {
                      const currentDays = form.getValues("daysOfWeek") || [];
                      form.setValue(
                        "daysOfWeek",
                        checked
                          ? ([...currentDays, day.id] as [string, ...string[]]) // Add selected day
                          : (currentDays.filter((d) => d !== day.id) as [
                              string,
                              ...string[]
                            ]) // Remove unselected day
                      );
                    }}
                  />
                  <span>{day.name}</span>
                </label>
              ))}
            </div>
          </FormControl>
          <FormDescription>
            Select the days of the week for the schedule.
          </FormDescription>
          <FormMessage />
        </FormItem>
        <div className="grid grid-cols-2 gap-2">
          <FormField
            control={form.control}
            name="startTime"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Start Time</FormLabel>
                <FormControl>
                  <Input type="time" {...field} />
                </FormControl>
                <FormDescription>
                  Enter the start time for each study day.
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="endTime"
            render={({ field }) => (
              <FormItem>
                <FormLabel>End Time</FormLabel>
                <FormControl>
                  <Input type="time" {...field} />
                </FormControl>
                <FormDescription>
                  Enter the end time for each study day.
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <div>
          <Button type="submit">Create Schedule</Button>
          <Button variant="ghost" onClick={onClose}>
            Cancel
          </Button>
        </div>
      </form>
    </Form>
  );
}
