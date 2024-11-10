import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogDescription
} from "@/components/ui/dialog";
import { AddDegreeForms } from "@/components/dialogforms/addDegreeForms";

export function AddDegreeDialog({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) {

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
}