import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { ModuleForm } from "@/components/dialogforms/module-form";
import { Module } from "@/types";

type ModuleDialogProps = {
  isOpen: boolean;
  onClose: () => void;
  isEdit: boolean;
  module?: Module | undefined;
};

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
        <ModuleForm
          defaultValues={
            isEdit && module
              ? {
                  moduleName: module.name,
                  moduleDescription: module.description,
                  moduleECTS: module.ects,
                  moduleUnderstanding: module.understanding,
                  moduleTime: module.time,
                  moduleImportance: module.complexity,
                }
              : undefined
          }
          onClose={onClose}
          isEdit={isEdit}
          moduleId={module?.id}
        />
      </DialogContent>
    </Dialog>
  );
};

export { ModuleDialog };
