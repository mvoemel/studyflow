"use client";

import { Button } from "@/components/ui/button";
import { useState, useCallback, useMemo } from "react";
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
import { useParams } from "next/navigation";
import { ModuleDialog } from "@/components/dialogs/module-dialog";
import { Module } from "@/types";
import { useModules } from "@/hooks/use-modules";
import {PenIcon, Trash2} from "lucide-react";
import {toast} from "sonner";
import {
  AlertDialog, AlertDialogAction, AlertDialogCancel,
  AlertDialogContent, AlertDialogDescription, AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger
} from "@/components/ui/alert-dialog";

const ModulesSettingsPage = () => {
  const { degreeId, semesterId } = useParams();

  const { modules, deleteModule } = useModules();

  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
  const [selectedModule, setSelectedModule] = useState<Module | undefined>();

  const filteredModules = useMemo(() => {
    return (
      modules?.filter(
        (m) => m.degreeId === degreeId && m.semesterId === semesterId
      ) || []
    );
  }, [degreeId, semesterId, modules]);

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

  const handleDeleteModule = async (moduleId: string) => {
    try {
      await deleteModule(moduleId);
        toast.success("Successfully deleted module!");
    } catch (err) {
        toast.error("Failed to delete module!");
    }
  }

  return (
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 p-4 md:gap-8 md:p-10">
      <Card className="bg-muted/50">
        <CardHeader>
          <CardTitle>Modules</CardTitle>
          <CardDescription>Here you can add or remove modules.</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            {filteredModules.length === 0 && (
              <TableCaption>No Modules in this Semester.</TableCaption>
            )}
            <TableHeader>
              <TableRow>
                <TableHead>Module</TableHead>
                <TableHead>ECTS</TableHead>
                <TableHead>Complexity</TableHead>
                <TableHead>Time</TableHead>
                <TableHead>Understanding</TableHead>
                <TableHead>Action</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredModules.map((module) => (
                <TableRow
                  key={`${module.id}-${module.semesterId}-${module.degreeId}`}
                >
                  <TableCell className="font-medium">{module.name}</TableCell>
                  <TableCell>{module.ects}</TableCell>
                  <TableCell>{module.complexity + "/10"}</TableCell>
                  <TableCell>{module.time + "/10"}</TableCell>
                  <TableCell>{module.understanding + "/10"}</TableCell>
                  <TableCell>
                    <Button
                      className="p-0"
                      variant="ghost"
                      size="icon"
                      onClick={() => openEditDialog(module)}
                    >
                      <PenIcon className="h-4" />
                    </Button>
                    <AlertDialog>
                      <AlertDialogTrigger>
                        <Button
                            className="p-0"
                            variant="destructive"
                            size="icon"
                        >
                          <Trash2 className="h-4" />
                        </Button>
                      </AlertDialogTrigger>
                      <AlertDialogContent>
                        <AlertDialogHeader>
                          <AlertDialogTitle>
                            Are you sure you want to delete this module?
                          </AlertDialogTitle>
                          <AlertDialogDescription>
                            This action cannot be undone.
                          </AlertDialogDescription>
                        </AlertDialogHeader>
                        <AlertDialogFooter>
                          <AlertDialogCancel>Cancel</AlertDialogCancel>
                          <AlertDialogAction onClick={() => handleDeleteModule(module.id)}>
                            Continue
                          </AlertDialogAction>
                        </AlertDialogFooter>
                      </AlertDialogContent>
                    </AlertDialog>
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

      <ModuleDialog
        isOpen={isDialogOpen}
        onClose={closeEditDialog}
        isEdit={true}
        module={selectedModule}
      />

      <ModuleDialog
        isOpen={isAddDialogOpen}
        onClose={closeAddDialog}
        isEdit={false}
      />
    </div>
  );
};

export default ModulesSettingsPage;
