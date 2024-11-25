"use client";

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
import { useParams } from "next/navigation";
import { useDegree } from "@/providers/data-provider";
import { ModuleDialog } from "@/components/dialogs/moduleDialog";
import { LoadingSpinner } from "@/components/global/loading-spinner";

type Module = {
  id: number;
  degreeId: number;
  semesterId: number;
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
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { degreeId, semesterId } = useParams();
  const { degrees } = useDegree();
  const [title, setTitle] = useState<string>("");

  useEffect(() => {
    const degree = degrees.find(
      (degree) =>
        degree.id === parseInt(Array.isArray(degreeId) ? degreeId[0] : degreeId)
    );
    const semester = degree?.semesters.find(
      (semester) =>
        semester.id ===
        parseInt(Array.isArray(semesterId) ? semesterId[0] : semesterId)
    );
    if (degree && semester) {
      setTitle(`Modules for ${semester.name} of ${degree.name}`);
    }
  }, [degreeId, semesterId, degrees]);

  useEffect(() => {
    setLoading(true);
    const fetchModules = async () => {
      try {
        const response = await fetch("/api/modules");
        if (!response.ok) {
          throw new Error("Failed to fetch modules");
        }
        const data = await response.json();
        const filteredModules = data.filter(
          (module: Module) =>
            module.degreeId ===
              parseInt(Array.isArray(degreeId) ? degreeId[0] : degreeId) &&
            module.semesterId ===
              parseInt(Array.isArray(semesterId) ? semesterId[0] : semesterId)
        );
        setModules(filteredModules);
      } catch (error) {
        setError((error as Error).message);
      } finally {
        setLoading(false);
      }
    };

    fetchModules();
  }, [degreeId, semesterId]);

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
    <div className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
      <h1 className="text-3xl font-semibold">{title || "Select a Semester"}</h1>
      <Card className="max-w-[60%]">
        <CardHeader>
          <CardTitle>Modules</CardTitle>
          <CardDescription>Here you can add or remove modules.</CardDescription>
        </CardHeader>
        <CardContent>
          {loading && (
            <div className="w-full flex justify-center">
              <LoadingSpinner />
            </div>
          )}
          {!loading && (
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
                  <TableRow
                    key={`${module.id}-${module.degreeId}-${module.semesterId}`}
                  >
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
          )}
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
