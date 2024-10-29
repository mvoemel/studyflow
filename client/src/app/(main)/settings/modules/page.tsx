"use client"
import { Button } from "@/components/ui/button";
import { useEffect, useState, useCallback } from "react";
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
import { Progress } from "@/components/ui/progress"


type Module = {
    id: number;
    name: string;
    ECTS: string;
    Understanding: string;
    Time: string;
    Importance: string;
};

const ModulesSettingsPage = () => {
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
    const [selectedModule, setSelectedModule] = useState<Module | null>(null);
    const [modules, setModules] = useState<Module[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchModules = async () => {
            try {
                const response = await fetch('/api/modules');
                if (!response.ok) {
                    throw new Error('Failed to fetch modules');
                }
                const data = await response.json();
                setModules(data);
            } catch (error) {
                setError((error as Error).message);
            } finally {
                setLoading(false);
            }
        };

        fetchModules();
    }, []);

    const openEditDialog = useCallback((module: Module) => {
        setSelectedModule(module);
        setIsDialogOpen(true);
    }, []);

    const closeEditDialog = useCallback(() => {
        setIsDialogOpen(false);
    }, []);

    const openAddDialog = useCallback(() => {
        setIsAddDialogOpen(true);
    }, []);

    const closeAddDialog = useCallback(() => {
        setIsAddDialogOpen(false);
    }, []);

    if (error) {
        return <div>Error: {error}</div>;
    }

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
                                <TableRow key={module.id}>
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
                    <Button onClick={openAddDialog}>Add Module</Button>
                </CardFooter>
            </Card>

            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
                <DialogContent className="max-h-screen overflow-scroll">
                    <DialogHeader>
                        <DialogTitle>Edit Module</DialogTitle>
                        <DialogDescription>
                            Edit the details of the module.
                        </DialogDescription>
                    </DialogHeader>
                    {selectedModule && (
                        <ModuleForms
                            defaultValues={{
                                moduleName: selectedModule.name,
                                moduleDescription: "",
                                moduleECTS: parseInt(selectedModule.ECTS),
                                moduleUnderstanding: parseInt(selectedModule.Understanding),
                                moduleTime: parseInt(selectedModule.Time),
                                moduleImportance: parseInt(selectedModule.Importance),
                            }}
                        />
                    )}
                    <DialogFooter>
                        <Button onClick={closeEditDialog}>Close</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>

            <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
                <DialogContent className="max-h-screen overflow-scroll">
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

export default ModulesSettingsPage;