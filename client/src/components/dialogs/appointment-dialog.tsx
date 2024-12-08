import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import {
  AppointmentForm,
  AppointmentFormValues,
} from "@/components/dialogforms/appointment-form";

type AppointmentDialogProps = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSubmit: (data: AppointmentFormValues) => void;
  onDelete: () => void;
  initialData?: AppointmentFormValues | null;
};

const AppointmentDialog = ({
  open,
  onOpenChange,
  onSubmit,
  onDelete,
  initialData,
}: AppointmentDialogProps) => {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {initialData ? "Edit Appointment" : "Add an Appointment"}
          </DialogTitle>
          <DialogDescription>
            {initialData
              ? "Change details of this appointment."
              : "Fill in the details to add a new appointment."}
          </DialogDescription>
        </DialogHeader>
        <AppointmentForm
          initialData={initialData}
          onSubmit={onSubmit}
          onDelete={onDelete}
        />
      </DialogContent>
    </Dialog>
  );
};

export { AppointmentDialog };
