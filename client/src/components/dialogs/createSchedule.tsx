import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { CreateScheduleForms } from "@/components/dialogforms/createScheduleForms";
import { Module } from "@/context/ModuleContext";

export function CreateScheduleDialog({
  isOpen,
  onClose,
  modules,
}: {
  isOpen: boolean;
  onClose: () => void;
  modules: Module[];
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
        <CreateScheduleForms onClose={onClose} modules={modules} />
      </DialogContent>
    </Dialog>
  );
}
