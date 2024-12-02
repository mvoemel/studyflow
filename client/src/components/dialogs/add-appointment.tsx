import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { AddAppointmentForm } from "@/components/dialogforms/add-appointment-form";

export function AddAppointmentDialog({
  isOpen,
  onClose,
}: {
  isOpen: boolean;
  onClose: () => void;
}) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Add an Appointment</DialogTitle>
          <DialogDescription>
            Fill in the details to add a new appointment.
          </DialogDescription>
        </DialogHeader>
        <AddAppointmentForm onClose={onClose} />
      </DialogContent>
    </Dialog>
  );
}
