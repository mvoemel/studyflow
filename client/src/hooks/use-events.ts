import { useMemo } from "react";
import { useAppointments } from "./use-appointments";
import { useDegrees } from "./use-degree";
import { useSemesters } from "./use-semester";
import { useUserSettings } from "./use-user-settings";
import { EventSourceInput } from "@fullcalendar/core/index.js";

// BUG: there is no way to show if the data is currently loading
const useEvents = () => {
  const { settings } = useUserSettings();
  const { degrees } = useDegrees();
  const { semesters } = useSemesters();

  const currActiveCalendarId = useMemo(() => {
    if (!semesters || !settings?.activeDegreeId) return undefined;

    const currDegree = degrees?.find((d) => d.id === settings?.activeDegreeId);
    if (!currDegree || !currDegree.activeSemesterId) return undefined;

    const currSemester = semesters?.find(
      (s) => s.id === currDegree?.activeSemesterId
    );
    if (!currSemester) return undefined;

    return currSemester?.calendarId;
  }, [settings, degrees, semesters]);

  const { appointments: globalAppointments } = useAppointments(
    settings?.globalCalendarId
  );
  const { appointments: currSemesterAppointments } =
    useAppointments(currActiveCalendarId);

  const globalEvents: EventSourceInput =
    globalAppointments?.map((a) => ({
      ...a,
      start: a.startDateTime,
      end: a.endDateTime,
    })) || [];
  const currSemesterEvents: EventSourceInput =
    currSemesterAppointments?.map((a) => ({
      ...a,
      start: a.startDateTime,
      end: a.endDateTime,
    })) || [];

  return {
    events: globalEvents.concat(currSemesterEvents),
  };
};

export { useEvents };
