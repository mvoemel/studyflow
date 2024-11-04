import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { ModuleForms } from "@/components/dialogforms/moduleForms";

type Module = {
    id: number;
    degreeId: number;
    semesterId: number;
    name: string;
    ECTS: string;
    Understanding: string;
    Time: string;
    Importance: string;
}

type ModuleDialogProps = {
    isOpen: boolean;
    onClose: () => void;
    isEdit: boolean;
    module?: Module | null;
};

export const ModuleDialog = ({ isOpen, onClose, isEdit, module }: ModuleDialogProps) => {
    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="max-h-screen overflow-auto">
                <DialogHeader>
                    <DialogTitle>{isEdit ? "Edit Module" : "Add Module"}</DialogTitle>
                    <DialogDescription>
                        {isEdit ? "Edit the details of the module." : "Fill in the details for the new module."}
                    </DialogDescription>
                </DialogHeader>
                <ModuleForms
                    defaultValues={isEdit && module ? {
                        moduleName: module.name,
                        moduleDescription: "",
                        moduleECTS: parseInt(module.ECTS),
                        moduleUnderstanding: parseInt(module.Understanding),
                        moduleTime: parseInt(module.Time),
                        moduleImportance: parseInt(module.Importance),
                    } : undefined}
                    onClose={onClose}
                    isEdit={isEdit}
                />
            </DialogContent>
        </Dialog>
    );
};