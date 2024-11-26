"use client";

import { Degree, Module, Semester, Settings, User } from "@/types";
import {
  createContext,
  useContext,
  useState,
  useEffect,
  ReactNode,
} from "react";

type DataContextType = {
  user: User | undefined;
  settings: Settings | undefined;
  degrees: Degree[] | undefined;
  semesters: Semester[] | undefined;
  modules: Module[] | undefined;
  setActiveDegree: (degreeId: Degree["id"]) => void;
  setActiveSemester: (semesterId: Semester["id"]) => void;
  fetchUserAndSettings: () => Promise<void>;
  fetchDegrees: () => Promise<void>;
  fetchSemesters: () => Promise<void>;
  fetchModules: () => Promise<void>;
};

const DataContext = createContext<DataContextType | undefined>(undefined);

export const DataProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User>();
  const [settings, setSettings] = useState<Settings>();
  const [degrees, setDegrees] = useState<Degree[]>();
  const [semesters, setSemesters] = useState<Semester[]>();
  const [modules, setModules] = useState<Module[]>();

  useEffect(() => {
    console.log("Settings updated:", settings);
  }, [settings]);

  // BUG: if both setActiveDegree and setActiveSemesters are called simultaneously we have wrong behaviour

  const setActiveDegree = (degreeId: Degree["id"]) => {
    const newSettings = { ...settings };
    newSettings.activeDegreeId = degreeId;
    console.log("newSettings: " + JSON.stringify(newSettings));
    setSettings(newSettings); // TODO: properly check for type safety
    // TODO: POST /settings with new activeDegreeId
    console.log("Inside DataProvider"); // TODO: remove
    console.log("Given", degreeId);
    console.log(settings);
  };
  const setActiveSemester = (semesterId: Semester["id"]) => {
    setSettings({ ...settings, activeSemesterId: semesterId } as Settings); // TODO: properly check for type safety
    // TODO: POST /settings with new activeSemesterId
  };

  const fetchUserAndSettings = async () => {
    fetch("/api/auth/me")
      .then((response) => response.json())
      .then((data) => {
        if (data.user) setUser(data.user);
        if (data.settings) setSettings(data.settings);
      })
      .catch((error) => alert("Error fetching user and settings: " + error));
  };
  const fetchDegrees = async () => {
    fetch("/api/degrees")
      .then((response) => response.json())
      .then((data) => setDegrees(data))
      .catch((error) => alert("Error fetching degrees: " + error));
  };
  const fetchSemesters = async () => {
    fetch("/api/semesters")
      .then((response) => response.json())
      .then((data) => setSemesters(data))
      .catch((error) => alert("Error fetching semesters: " + error));
  };
  const fetchModules = async () => {
    fetch("/api/modules")
      .then((response) => response.json())
      .then((data) => setModules(data))
      .catch((error) => alert("Error fetching modules: " + error));
  };

  useEffect(() => {
    fetchUserAndSettings();
    fetchDegrees();
    fetchSemesters();
    fetchModules();
  }, []);

  return (
    <DataContext.Provider
      value={{
        user,
        settings,
        degrees,
        semesters,
        modules,
        setActiveDegree,
        setActiveSemester,
        fetchUserAndSettings,
        fetchDegrees,
        fetchSemesters,
        fetchModules,
      }}
    >
      {children}
    </DataContext.Provider>
  );
};

export const useData = () => {
  const context = useContext(DataContext);
  if (context === undefined) {
    throw new Error("useData must be used within a DataProvider");
  }
  return context;
};
