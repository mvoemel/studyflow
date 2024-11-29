import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";

// TODO: implement
const ProfileSettingsPage = () => {
  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <Card className="bg-muted/50">
        <CardHeader>
          <CardTitle>Profile</CardTitle>
          <CardDescription>
            Here you can change your profile information.
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form className="grid grid-cols-2 gap-4">
            <Input type="text" placeholder="Firstname" />
            <Input type="text" placeholder="Lastname" />
            <Input type="email" placeholder="Email" className="col-span-2" />
            <Input type="password" placeholder="Old Password" />
            <Input type="password" placeholder="New Password" />
          </form>
        </CardContent>
        <CardFooter className="border-t px-6 py-4 flex justify-end">
          <Button>Save</Button>
        </CardFooter>
      </Card>
    </div>
  );
};

export default ProfileSettingsPage;
