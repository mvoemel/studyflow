import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { AddSemesterForm } from "@/components/dialogforms/add-semester-form";

export function AddSemesterDialog({
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
          <DialogTitle>Add Semester</DialogTitle>
          <DialogDescription>
            Fill in the details for a new semester.
          </DialogDescription>
        </DialogHeader>
        <AddSemesterForm onClose={onClose} />
      </DialogContent>
    </Dialog>
  );
}
