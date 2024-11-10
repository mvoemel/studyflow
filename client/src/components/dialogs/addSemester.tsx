import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogDescription
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { AddSemesterForms } from "@/components/dialogforms/addSemesterForms";

export function AddSemesterDialog({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) {

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Add Semester</DialogTitle>
                    <DialogDescription>
                        Fill in the details for a new semester.
                    </DialogDescription>
                </DialogHeader>
                <AddSemesterForms onClose={onClose} />
            </DialogContent>
        </Dialog>
    );
}