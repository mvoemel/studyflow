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


const formsSchema = z.object({
    appointmentTitle: z.string().min(2, "Degree Title must be at least 2 characters long").max(50),
    appointmentDescription: z.string().max(200, "Degree description must be at most 200 characters long"),
    appointmentDate: z.string().date("Invalid date"),
})

type AddApointmentFormsProps = {
    defaultValues?: Partial<z.infer<typeof formsSchema>>;
    onClose: () => void;
}


export function AddAppointmentForms({defaultValues, onClose}: AddApointmentFormsProps) {
    const form = useForm<z.infer<typeof formsSchema>>({
        resolver: zodResolver(formsSchema),
        defaultValues: defaultValues || {
            appointmentTitle: "",
            appointmentDescription: "",
            appointmentDate: "",
        },
    })

    function onSubmit(values: z.infer<typeof formsSchema>) {
        console.log(values)
        onClose()
    }

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
                    name="appointmentDate"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Appointment Date</FormLabel>
                            <FormControl>
                                <Input type="date" {...field} />
                            </FormControl>
                            <FormDescription>
                                Enter the date of the appointment.
                            </FormDescription>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <Button type="submit">Add</Button>
                <Button variant="ghost" onClick={onClose}>Cancel</Button>
            </form>
        </Form>
    )
}