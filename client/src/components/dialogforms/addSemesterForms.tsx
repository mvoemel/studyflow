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
    semesterName: z.string().min(2, "Semester Name must be at least 2 characters long").max(50),
    semesterDescription: z.string().max(200, "Semester description must be at most 200 characters long"),
})

type AddSemesterFormsProps = {
    defaultValues?: Partial<z.infer<typeof formsSchema>>;
    onClose: () => void;
}


export function AddSemesterForms({defaultValues, onClose}: AddSemesterFormsProps) {
    const form = useForm<z.infer<typeof formsSchema>>({
        resolver: zodResolver(formsSchema),
        defaultValues: defaultValues || {
            semesterName: "",
            semesterDescription: "",
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
                    name="semesterName"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Semester Name</FormLabel>
                            <FormControl>
                                <Input placeholder="Semester Name" {...field} />
                            </FormControl>
                            <FormDescription>
                                Enter the name of the Semester.
                            </FormDescription>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="semesterDescription"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Semester Description</FormLabel>
                            <FormControl>
                                <Textarea placeholder="Semester Description" {...field} />
                            </FormControl>
                            <FormDescription>
                                Enter a brief description of the semester.
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