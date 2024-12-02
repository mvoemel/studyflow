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
import {useFieldArray, useForm} from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {z} from "zod";
import { toast } from "sonner";
import {cn} from "@/lib/utils";

const formsSchema = z.object({
    grades: z.array(
        z.object({
            name: z.string().min(2, "Grade title must be at least 2 characters long"),
            value: z.number().min(1, "Grade value must be at least 1"),
            percentage: z.number().min(0.01, "Grade percentage must be at least 0.01"),
        })
    )
});

type AddGradeFormsProps = {
    defaultValues?: Partial<z.infer<typeof formsSchema>>;
    onClose: () => void;
};

export function AddGradesForms({
                                    defaultValues,
                                    onClose,
                                }: AddGradeFormsProps) {

    const form = useForm<z.infer<typeof formsSchema>>({
        resolver: zodResolver(formsSchema),
        defaultValues: defaultValues || {
            grades: [{ name:"", value: 0 , percentage: 0 }],
        },
    });

    const { fields, append } = useFieldArray({
        name: "grades",
        control: form.control,
    })

    const onSubmit = async (values: z.infer<typeof formsSchema>) => {
        const totalPercentage = values.grades.reduce((sum, grade) => sum + grade.percentage, 0);
        if (totalPercentage > 1) {
            toast.error("The total percentage of all grades should not exceed 100%");
            return;
        }
        console.log(values);
        onClose();
    };

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                <div>
                    {fields.map((field, index) => (
                        <div key={`${field.id}-${index}`} className="space-y-1 mb-8">
                            <FormField
                                control={form.control}
                                name={`grades.${index}.name`}
                                render={({ field }) => (
                                    <FormItem>
                                        <FormLabel className={cn(index !== 0 && "sr-only")}>
                                            Title and Grade
                                        </FormLabel>
                                        <FormDescription className={cn(index !== 0 && "sr-only")}>
                                            Add the title of the exam and the according grade.
                                        </FormDescription>
                                        <FormControl>
                                            <Input placeholder={"Exam Title"} {...field} />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                            />
                            <div className="grid grid-cols-2 gap-4">
                        <FormField
                            control={form.control}
                            name={`grades.${index}.value`}
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel className={cn(index !== 0 && "sr-only")}>

                                    </FormLabel>
                                    <FormDescription className={cn(index !== 0 && "sr-only")}>

                                    </FormDescription>
                                    <FormControl>
                                        <Input
                                            type="number"
                                            step="0.01"
                                            placeholder={"Exam grade"} {...field}
                                            onChange={(e) => field.onChange(parseFloat(e.target.value))}
                                        />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                        <FormField
                            control={form.control}
                            name={`grades.${index}.percentage`}
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel className={cn(index !== 0 && "sr-only")}>

                                    </FormLabel>
                                    <FormDescription className={cn(index !== 0 && "sr-only")}>

                                    </FormDescription>
                                    <FormControl>
                                        <Input
                                            type="number"
                                            step="0.01"
                                            placeholder={"Exam percentage e.g. (0.5)"} {...field}
                                            onChange={(e) => field.onChange(parseFloat(e.target.value))}
                                        />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />
                            </div>
                    </div>
                    ))}
                    <Button
                        type="button"
                        variant="outline"
                        size="sm"
                        className="mt-2"
                        onClick={() => append({name:"", value: 0, percentage: 0})}
                    >
                        Add Grade
                    </Button>
                </div>
                <Button type="submit">Add</Button>
                <Button variant="ghost" onClick={onClose}>
                    Cancel
                </Button>
            </form>
        </Form>
    );
}
