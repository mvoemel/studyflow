import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { ModuleForms } from "@/components/dialogforms/moduleForms";
import { Module } from "@/types";

type ModuleDialogProps = {
  isOpen: boolean;
  onClose: () => void;
  isEdit: boolean;
  module?: Module | undefined;
};

// TODO: refactor
const ModuleDialog = ({
  isOpen,
  onClose,
  isEdit,
  module,
}: ModuleDialogProps) => {
  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="max-h-screen overflow-auto">
        <DialogHeader>
          <DialogTitle>{isEdit ? "Edit Module" : "Add Module"}</DialogTitle>
          <DialogDescription>
            {isEdit
              ? "Edit the details of the module."
              : "Fill in the details for the new module."}
          </DialogDescription>
        </DialogHeader>
        <ModuleForms
          defaultValues={
            isEdit && module
              ? {
                  moduleName: module.name,
                  moduleDescription: "",
                  moduleECTS: module.ects,
                  moduleUnderstanding: module.understanding,
                  moduleTime: module.time,
                  moduleImportance: module.complexity,
                }
              : undefined
          }
          onClose={onClose}
          isEdit={isEdit}
        />
      </DialogContent>
    </Dialog>
  );
};

export { ModuleDialog };
