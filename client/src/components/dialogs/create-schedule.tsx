import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { CreateScheduleForm } from "@/components/dialogforms/create-schedule-form";
import { Module } from "@/types";

export function CreateScheduleDialog({
  isOpen,
  onClose,
}: {
  isOpen: boolean;
  onClose: () => void;
}) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-[50%] max-h-[90%] overflow-auto">
        <DialogHeader>
          <DialogTitle>Generate a Study plan</DialogTitle>
          <DialogDescription>
            Fill in all the details to generate a study plan.
          </DialogDescription>
        </DialogHeader>
        <CreateScheduleForm onClose={onClose} />
      </DialogContent>
    </Dialog>
  );
}
