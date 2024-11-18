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

const formsSchema = z.object({
    startDate: z.string().min(1, "Start date is required"),
    endDate: z.string().min(1, "End date is required"),
    daysOfWeek: z.array(z.string()).nonempty("At least one day is required"),
    startTime: z.string().min(1, "Start time is required"),
    endTime: z.string().min(1, "End time is required"),
    examDates: z.record(z.string(), z.string().min(1, "Exam date is required")),
});

const daysOfWeek = [
    { id: "sunday", name: "Sunday" },
    { id: "monday", name: "Monday" },
    { id: "tuesday", name: "Tuesday" },
    { id: "wednesday", name: "Wednesday" },
    { id: "thursday", name: "Thursday" },
    { id: "friday", name: "Friday" },
    { id: "saturday", name: "Saturday" },
];

type CreateScheduleFormsProps = {
    defaultValues?: Partial<z.infer<typeof formsSchema>>;
    onClose: () => void;
    modules: any[];
};

export function CreateScheduleForms({ defaultValues, onClose, modules }: CreateScheduleFormsProps) {
    const form = useForm<z.infer<typeof formsSchema>>({
        resolver: zodResolver(formsSchema),
        defaultValues: defaultValues || {
            startDate: "",
            endDate: "",
            daysOfWeek: [], // Ensure this is a non-empty array
            startTime: "",
            endTime: "",
            examDates: modules.reduce((acc, module) => {
                acc[module.name] = "";
                return acc;
            }, {} as Record<string, string>),
        },
    });

    function onSubmit(values: z.infer<typeof formsSchema>) {
        console.log(values);
        onClose();
    }
    // Debugging...
    const { errors } = form.formState;
    console.log("Validation Errors:", errors);
    console.log("daysOfWeek:", form.watch("daysOfWeek"));

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                <div className="flex gap-4">
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
                            {daysOfWeek.map((day) => (
                                <label key={day.id} className="inline-flex items-center space-x-2">
                                    <Checkbox
                                        value={day.id}
                                        checked={form.watch("daysOfWeek").includes(day.id)}
                                        onCheckedChange={(checked) => {
                                            const currentDays = form.getValues("daysOfWeek") || [];
                                            form.setValue(
                                                "daysOfWeek",
                                                checked
                                                    ? [...currentDays, day.id] // Add selected day
                                                    : currentDays.filter((d) => d !== day.id) // Remove unselected day
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
                    <FormMessage/>
                </FormItem>
                <div className="flex gap-4">
                    <FormField
                        control={form.control}
                        name="startTime"
                        render={({field}) => (
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
                    <h2 className="text-xl font-semibold">Exam Dates for Modules</h2>
                    <div className="grid gap-4">
                        {modules.map((module) => (
                            <FormField
                                key={module.id}
                                control={form.control}
                                name={`examDates.${module.name}`}
                                render={({ field }) => (
                                    <FormItem className="flex items-center justify-between">
                                        <FormLabel className="w-1/2">{module.name}</FormLabel>
                                        <FormControl className="w-1/2">
                                            <Input type="date" {...field} />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                        ))}
                    </div>
                </div>
                <div>
                    <Button type="submit">Create Schedule</Button>
                    <Button variant="ghost" onClick={onClose}>Cancel</Button>
                </div>
            </form>
        </Form>
    );
}