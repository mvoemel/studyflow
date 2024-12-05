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
import { useSemesters } from "@/hooks/use-semester";
import { toast } from "sonner";
import { useUserSettings } from "@/hooks/use-user-settings";

const formsSchema = z.object({
  semesterName: z
    .string()
    .min(2, "Semester Name must be at least 2 characters long")
    .max(50),
  semesterDescription: z
    .string()
    .max(200, "Semester description must be at most 200 characters long"),
});

type AddSemesterFormsProps = {
  defaultValues?: Partial<z.infer<typeof formsSchema>>;
  onClose: () => void;
  isEdit: boolean;
  semesterId?: string;
};

export function AddSemesterForm({
  defaultValues,
  onClose,
  isEdit,
  semesterId,
}: AddSemesterFormsProps) {
  const { settings } = useUserSettings();
  const { addNewSemester, updateSemester } = useSemesters();

  const form = useForm<z.infer<typeof formsSchema>>({
    resolver: zodResolver(formsSchema),
    defaultValues: defaultValues || {
      semesterName: "",
      semesterDescription: "",
    },
  });

  const onSubmit = async (values: z.infer<typeof formsSchema>) => {
    onClose();

    try {
        if (!settings?.activeDegreeId) throw new Error("No active degree");

            if (isEdit) {
                await updateSemester(
                    {
                        name: values.semesterName,
                        description: values.semesterDescription,
                    },
                    semesterId as string
                );

            } else {
                await addNewSemester({
                    name: values.semesterName,
                    description: values.semesterDescription,
                    degreeId: settings.activeDegreeId,
                });
            }

            toast.success(`Successfully ${isEdit ? "updated" : "added"} semester.`);
        } catch (err) {
            toast.error(`Failed to ${isEdit ? "update" : "add"} semester.`);
        }
  };

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
              <FormDescription>Enter the name of the Semester.</FormDescription>
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
        <Button
            variant="ghost"
            onClick={(event) => {
                event.preventDefault();
                onClose();
            }}
        >
          Cancel
        </Button>
      </form>
    </Form>
  );
}
