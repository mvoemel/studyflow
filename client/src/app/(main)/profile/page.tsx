"use client";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/forms";
import { Input } from "@/components/ui/input";
import { useUserSettings } from "@/hooks/use-user-settings";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { toast } from "sonner";
import { z } from "zod";

const profileFormSchema = z.object({
  firstname: z.string(),
  lastname: z.string(),
  email: z.string().email("This is not a valid email"),
  password: z
    .string()
    .min(4, "Password needs to be at least 4 chars long")
    .max(24, "Password needs to be at most 24 chars long")
    .optional(),
});

// BUG: if you reload page data is not loaded into form (because data was not fetched yet but form already rendered)
const ProfileSettingsPage = () => {
  const { user, updateUser } = useUserSettings();

  const form = useForm<z.infer<typeof profileFormSchema>>({
    resolver: zodResolver(profileFormSchema),
    defaultValues: {
      firstname: user?.firstname,
      lastname: user?.lastname,
      email: user?.email,
      password: "",
    },
  });

  const onSubmit = async (values: z.infer<typeof profileFormSchema>) => {
    try {
      await updateUser(values);

      toast.success("Successfully updated profile!");
    } catch (err) {
      toast.error("Failed to update profile!");
    }
  };

  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
          <Card className="bg-muted/50">
            <CardHeader>
              <CardTitle>Profile</CardTitle>
              <CardDescription>
                Here you can change your profile information.
              </CardDescription>
            </CardHeader>
            <CardContent className="grid md:grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="firstname"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Firstname</FormLabel>
                    <FormControl>
                      <Input placeholder="Firstname" {...field} />
                    </FormControl>
                    <FormDescription>Enter your firstname.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="lastname"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Lastname</FormLabel>
                    <FormControl>
                      <Input placeholder="Lastname" {...field} />
                    </FormControl>
                    <FormDescription>Enter your lastname.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem className="md:col-span-2">
                    <FormLabel>Email</FormLabel>
                    <FormControl>
                      <Input type="email" placeholder="Email" {...field} />
                    </FormControl>
                    <FormDescription>Enter your email.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem className="md:col-span-2">
                    <FormLabel>Password</FormLabel>
                    <FormControl>
                      <Input
                        type="password"
                        placeholder="Password"
                        {...field}
                      />
                    </FormControl>
                    <FormDescription>Enter your new password.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </CardContent>
            <CardFooter className="border-t px-6 py-4 flex justify-end">
              <Button type="submit">Save</Button>
            </CardFooter>
          </Card>
        </form>
      </Form>
    </div>
  );
};

export default ProfileSettingsPage;
