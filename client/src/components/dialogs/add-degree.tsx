import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { AddDegreeForms } from "@/components/dialogforms/add-degree-form";

type AddDegreeDialogProps = {
  isOpen: boolean;
  onClose: () => void;
};

const AddDegreeDialog = ({ isOpen, onClose }: AddDegreeDialogProps) => {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Add Degree</DialogTitle>
          <DialogDescription>
            Fill in the details for a new degree.
          </DialogDescription>
        </DialogHeader>
        <AddDegreeForms onClose={onClose} />
      </DialogContent>
    </Dialog>
  );
};

export { AddDegreeDialog };
