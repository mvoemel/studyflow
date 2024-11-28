"use client";
import { createContext, useContext, useState, useEffect, ReactNode } from 'react';

export type Module = {
    id: number;
    degreeId: number;
    semesterId: number;
    name: string;
    ECTS: number;
    Understanding: number;
    Time: number;
    Importance: number;
};

type ModuleContextType = {
    modules: Module[];
    fetchModules: () => void;
};

const ModuleContext = createContext<ModuleContextType | undefined>(undefined);

export const ModuleProvider = ({ children }: { children: ReactNode }) => {
    const [modules, setModules] = useState<Module[]>([]);

    const fetchModules = () => {
        fetch('/api/modules')
            .then(response => response.json())
            .then(data => setModules(data))
            .catch(error => console.error('Error fetching modules:', error));
    };

    useEffect(() => {
        fetchModules();
    }, []);

    return (
        <ModuleContext.Provider value={{ modules, fetchModules }}>
            {children}
        </ModuleContext.Provider>
    );
};

export const useModule = () => {
    const context = useContext(ModuleContext);
    if (context === undefined) {
        throw new Error('useModule must be used within a ModuleProvider');
    }
    return context;
};