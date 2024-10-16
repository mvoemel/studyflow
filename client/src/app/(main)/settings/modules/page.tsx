"use client"
import { Button } from "@/components/ui/button";
import { useState } from "react";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogFooter
} from "@/components/ui/dialog";
import { ModuleForms } from "@/components/settings/modules/moduleForms";

// Define a type for the module structure
type Module = {
    id: number;
    name: string;
    ECTS: string;
    Understanding: string;
    Time: string;
    Importance: string;
};

const modules: Module[] = [
    {
        id: 1,
        name: "Web Development",
        ECTS: "4",
        Understanding: "3",
        Time: "2",
        Importance: "4",
    },
    {
        id: 2,
        name: "Software Development",
        ECTS: "4",
        Understanding: "3",
        Time: "2",
        Importance: "4",
    },
    {
        id: 3,
        name: "Algorithms and Data Structures",
        ECTS: "4",
        Understanding: "3",
        Time: "4",
        Importance: "8",
    }
];

const ProfileSettingsPage = () => {
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
    const [selectedModule, setSelectedModule] = useState<Module | null>(null);

    const openEditDialog = (module: Module) => {
        console.log("Opening dialog for module:", module.name); // Debugging
        setSelectedModule(module);
        setIsDialogOpen(true); // This will trigger the dialog to open
    };

    const closeEditDialog = () => {
        console.log("Closing dialog");
        setIsDialogOpen(false); // This will trigger the dialog to close
    };

    const openAddDialog = () => {
        setIsAddDialogOpen(true);
    };

    const closeAddDialog = () => {
        setIsAddDialogOpen(false);
    };

    return (
        <>
            <Card>
                <CardHeader>
                    <CardTitle>Modules</CardTitle>
                    <CardDescription>
                        Here you can add or remove modules.
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <Table>
                        <TableCaption>List of your current modules.</TableCaption>
                        <TableHeader>
                            <TableRow>
                                <TableHead>Module</TableHead>
                                <TableHead>ECTS</TableHead>
                                <TableHead>Understanding</TableHead>
                                <TableHead>Time</TableHead>
                                <TableHead>Importance</TableHead>
                                <TableHead>Action</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {modules.map((module) => (
                                <TableRow key={module.name}>
                                    <TableCell className="font-medium">{module.name}</TableCell>
                                    <TableCell>{module.ECTS}</TableCell>
                                    <TableCell>{module.Understanding + "/10"}</TableCell>
                                    <TableCell>{module.Time + "/10"}</TableCell>
                                    <TableCell>{module.Importance + "/10"}</TableCell>
                                    <TableCell>
                                        <button onClick={() => openEditDialog(module)}>
                                            <svg
                                                xmlns="http://www.w3.org/2000/svg"
                                                width="24"
                                                height="24"
                                                viewBox="0 0 24 24"
                                                fill="none"
                                                stroke="currentColor"
                                                strokeWidth="2"
                                                strokeLinecap="round"
                                                strokeLinejoin="round"
                                                className="lucide lucide-ellipsis"
                                            >
                                                <circle cx="12" cy="12" r="1" />
                                                <circle cx="19" cy="12" r="1" />
                                                <circle cx="5" cy="12" r="1" />
                                            </svg>
                                        </button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </CardContent>
                <CardFooter className="border-t px-6 py-4">
                    <Button onClick={() => openAddDialog()}>Add Module</Button>
                </CardFooter>
            </Card>

            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Edit Module</DialogTitle>
                        <DialogDescription>
                            Edit the details of the module.
                        </DialogDescription>
                    </DialogHeader>
                    <div>
                        <p>Module Name: {selectedModule?.name}</p>
                        <p>Module ID: {selectedModule?.id}</p>
                    </div>
                    <DialogFooter>
                        <Button onClick={closeEditDialog}>Close</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>

            <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Add Module</DialogTitle>
                        <DialogDescription>
                            Fill in the details for the new module.
                        </DialogDescription>
                    </DialogHeader>
                    <ModuleForms />
                    <DialogFooter>
                        <Button onClick={closeAddDialog}>Close</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </>
    );
};

export default ProfileSettingsPage;