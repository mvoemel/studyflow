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

// Define a schema for the module structure
const formsSchema = z.object({
    moduleName: z.string().min(2, "Module name must be at least 2 characters long").max(50),
    moduleDescription: z.string().max(200, "Module description must be at most 200 characters long"),
    moduleECTS: z.number().int().min(0, "ECTS must be a positive number").max(12, "ECTS must be at most 12"),
    moduleUnderstanding: z.number().int().min(0, "Understanding must be a positive number").max(10, "Understanding must be at most 10"),
    moduleTime: z.number().int().min(0, "Time must be a positive number").max(10, "Time must be at most 10"),
    moduleImportance: z.number().int().min(0, "Importance must be a positive number").max(10, "Importance must be at most 10"),
})

type ModuleFormsProps = {
    defaultValues?: Partial<z.infer<typeof formsSchema>>;
}

// Define your forms
export function ModuleForms({defaultValues}: ModuleFormsProps) {
    const form = useForm<z.infer<typeof formsSchema>>({
        resolver: zodResolver(formsSchema),
        defaultValues: defaultValues || {
            moduleName: "",
            moduleDescription: "",
            moduleECTS: 0,
            moduleUnderstanding: 0,
            moduleTime: 0,
            moduleImportance: 0,
        },
    })

    function onSubmit(values: z.infer<typeof formsSchema>) {
        console.log(values)
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                <FormField
                    control={form.control}
                    name="moduleName"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Module Name</FormLabel>
                            <FormControl>
                                <Input placeholder="Module Name" {...field} />
                            </FormControl>
                            <FormDescription>
                                Enter the name of the module.
                            </FormDescription>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="moduleDescription"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Module Description</FormLabel>
                            <FormControl>
                                <Textarea placeholder="Module Description" {...field} />
                            </FormControl>
                            <FormDescription>
                                Enter a brief description of the module.
                            </FormDescription>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="moduleECTS"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>ECTS</FormLabel>
                            <FormControl>
                                <Input type="number" placeholder="ECTS" {...field} onChange={(e) => field.onChange(Number(e.target.value))} />
                            </FormControl>
                            <FormDescription>
                                Enter the ECTS value for the module.
                            </FormDescription>
                            <FormMessage />
                        </FormItem>
                    )}
                />
                <div className="flex space-x-4">
                    <FormField
                        control={form.control}
                        name="moduleUnderstanding"
                        render={({ field }) => (
                            <FormItem className="flex-1">
                                <FormLabel>Understanding</FormLabel>
                                <FormControl>
                                    <Input type="number" placeholder="Understanding" {...field} onChange={(e) => field.onChange(Number(e.target.value))} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="moduleTime"
                        render={({ field }) => (
                            <FormItem className="flex-1">
                                <FormLabel>Time</FormLabel>
                                <FormControl>
                                    <Input type="number" placeholder="Time"{...field} onChange={(e) => field.onChange(Number(e.target.value))}/>
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={form.control}
                        name="moduleImportance"
                        render={({ field }) => (
                            <FormItem className="flex-1">
                                <FormLabel>Importance</FormLabel>
                                <FormControl>
                                    <Input type="number" placeholder="Importance" {...field} onChange={(e) => field.onChange(Number(e.target.value))} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                </div>
                <FormDescription>
                    Rate these values from 0-10.
                </FormDescription>
                <Button type="submit">Submit</Button>
            </form>
        </Form>
    )
}