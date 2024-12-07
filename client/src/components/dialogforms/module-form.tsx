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
import { useModules } from "@/hooks/use-modules";
import { toast } from "sonner";
import { useParams } from "next/navigation";

const formsSchema = z.object({
  moduleName: z
    .string()
    .min(2, "Module name must be at least 2 characters long")
    .max(50),
  moduleDescription: z
    .string()
    .max(200, "Module description must be at most 200 characters long")
    .optional(),
  moduleECTS: z
    .number()
    .int()
    .min(0, "ECTS must be a positive number")
    .max(12, "ECTS must be at most 12"),
  moduleUnderstanding: z
    .number()
    .int()
    .min(0, "Understanding must be a positive number")
    .max(10, "Understanding must be at most 10"),
  moduleTime: z
    .number()
    .int()
    .min(0, "Time must be a positive number")
    .max(10, "Time must be at most 10"),
  moduleImportance: z
    .number()
    .int()
    .min(0, "Importance must be a positive number")
    .max(10, "Importance must be at most 10"),
});

type ModuleFormsProps = {
  onClose: () => void;
  isEdit: boolean;
  defaultValues?: Partial<z.infer<typeof formsSchema>>;
  moduleId?: string;
};

// TODO: refactor so that whole module is passed
// Attention: right now of no moduleId is passed when isEdit is true, it could break the app
const ModuleForm = ({
  onClose,
  isEdit,
  defaultValues,
  moduleId,
}: ModuleFormsProps) => {
  // TODO: refactor the use of params here
  const { degreeId, semesterId } = useParams();

  const { addNewModule, updateModule } = useModules();

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
  });

  const onSubmit = async (values: z.infer<typeof formsSchema>) => {
    onClose();

    if (!degreeId || !semesterId) {
      console.warn(
        "ModuleForm component was used outside a path that has the degreeId and semesterId"
      );
      return;
    }

    try {
      if (isEdit) {
        await updateModule(
          {
            name: values.moduleName,
            description: values.moduleDescription,
            ects: values.moduleECTS,
            understanding: values.moduleUnderstanding,
            time: values.moduleTime,
            complexity: values.moduleImportance,
          },
          moduleId as string
        );
      } else {
        await addNewModule({
          name: values.moduleName,
          description: values.moduleDescription,
          ects: values.moduleECTS,
          understanding: values.moduleUnderstanding,
          time: values.moduleTime,
          complexity: values.moduleImportance,
          degreeId: degreeId as string,
          semesterId: semesterId as string,
        });
      }

      toast.success(`Successfully ${isEdit ? "updated" : "added"}  module!`);
    } catch (err) {
        console.log(err);
      toast.error(`Failed to ${isEdit ? "update" : "add"} module.`);
    }
  };

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
              <FormDescription>Enter the name of the module.</FormDescription>
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
                <Input
                  type="number"
                  placeholder="ECTS"
                  {...field}
                  onChange={(e) => field.onChange(Number(e.target.value))}
                />
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
                  <Input
                    type="number"
                    placeholder="Understanding"
                    {...field}
                    onChange={(e) => field.onChange(Number(e.target.value))}
                  />
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
                  <Input
                    type="number"
                    placeholder="Time"
                    {...field}
                    onChange={(e) => field.onChange(Number(e.target.value))}
                  />
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
                  <Input
                    type="number"
                    placeholder="Importance"
                    {...field}
                    onChange={(e) => field.onChange(Number(e.target.value))}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
        <FormDescription>Rate these values from 0-10.</FormDescription>
        <Button type="submit">{isEdit ? "Save" : "Add"}</Button>
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
};

export { ModuleForm };
