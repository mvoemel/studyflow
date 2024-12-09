import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { AddGradesForms } from "@/components/dialogforms/add-grade-form";
import { Grade } from "@/types";

export function AddGradeDialog({
  isOpen,
  onClose,
  moduleId,
  grades,
}: {
  isOpen: boolean;
  onClose: () => void;
  moduleId: string;
  grades: Omit<Grade, "moduleId">[];
}) {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Add a Grade to the module</DialogTitle>
          <DialogDescription>Add a new Grade to the module.</DialogDescription>
        </DialogHeader>
        <AddGradesForms
          defaultValues={{ grades }}
          moduleId={moduleId}
          onClose={onClose}
        />
      </DialogContent>
    </Dialog>
  );
}
