import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from "@/components/ui/dialog";
import { CreateScheduleForm } from "@/components/dialogforms/create-schedule-form";

type CreateScheduleDialogProps = {
  isOpen: boolean;
  onClose: () => void;
  semesterId?: string;
  settingsId?: string;
};

const CreateScheduleDialog = ({
  isOpen,
  onClose,
  semesterId,
  settingsId,
}: CreateScheduleDialogProps) => {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-w-[50%] max-h-[90%] overflow-auto">
        <DialogHeader>
          <DialogTitle>Generate a Study plan</DialogTitle>
          <DialogDescription>
            Fill in all the details to generate a study plan.
          </DialogDescription>
        </DialogHeader>
        <CreateScheduleForm
          onClose={onClose}
          semesterId={semesterId}
          settingsId={settingsId}
        />
      </DialogContent>
    </Dialog>
  );
};

export { CreateScheduleDialog };
