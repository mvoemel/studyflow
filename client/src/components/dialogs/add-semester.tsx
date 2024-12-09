import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { AddSemesterForm } from "@/components/dialogforms/add-semester-form";
import { Semester } from "@/types";

type SemesterDialogProps = {
  isOpen: boolean;
  onClose: () => void;
  isEdit: boolean;
  semester?: Semester | undefined;
};

const AddSemesterDialog = ({
  isOpen,
  onClose,
  isEdit,
  semester,
}: SemesterDialogProps) => {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Add Semester</DialogTitle>
          <DialogDescription>
            Fill in the details for a new semester.
          </DialogDescription>
        </DialogHeader>
        <AddSemesterForm
          onClose={onClose}
          isEdit={isEdit}
          semesterId={semester?.id}
          defaultValues={
            isEdit && semester
              ? {
                  semesterName: semester.name,
                  semesterDescription: semester.description,
                }
              : undefined
          }
        />
      </DialogContent>
    </Dialog>
  );
};

export { AddSemesterDialog };
