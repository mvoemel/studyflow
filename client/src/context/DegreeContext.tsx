"use client"
import { createContext, useContext, useState, useEffect, ReactNode } from 'react';

export type Semester = {
    id: number;
    name: string;
    isActive: boolean;
};

export type Degree = {
    id: number;
    name: string;
    semesters: Semester[];
    isActive: boolean;
};

type DegreeContextType = {
    selectedDegree: Degree | null;
    setSelectedDegree: (degree: Degree) => void;
    activeSemester: Semester | null;
    setActiveSemester: (semester: Semester) => void;
    degrees: Degree[];
    fetchDegrees: () => void;
};

const DegreeContext = createContext<DegreeContextType | undefined>(undefined);

export const DegreeProvider = ({ children }: { children: ReactNode }) => {
    const [selectedDegree, setSelectedDegree] = useState<Degree | null>(null);
    const [activeSemester, setActiveSemester] = useState<Semester | null>(null);
    const [degrees, setDegrees] = useState<Degree[]>([]);

    const fetchDegrees = () => {
        fetch('/api/degree')
            .then(response => response.json())
            .then(data => {
                setDegrees(data);
                const activeDegree = data.find((degree: Degree) => degree.isActive);
                if (activeDegree) {
                    setSelectedDegree(activeDegree);
                    const activeSem = activeDegree.semesters.find((sem: Semester) => sem.isActive);
                    if (activeSem) {
                        setActiveSemester(activeSem);
                    }
                }
            })
            .catch(error => console.error('Error fetching degrees:', error));
    };

    useEffect(() => {
        fetchDegrees();
    }, []);

    return (
        <DegreeContext.Provider value={{ selectedDegree, setSelectedDegree, activeSemester, setActiveSemester, degrees, fetchDegrees }}>
            {children}
        </DegreeContext.Provider>
    );
};

export const useDegree = () => {
    const context = useContext(DegreeContext);
    if (context === undefined) {
        throw new Error('useDegree must be used within a DegreeProvider');
    }
    return context;
};