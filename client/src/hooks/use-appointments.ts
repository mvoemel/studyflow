import {
  AppointmentsForCalendarResponseData,
  deleteAppointmentForCalendarRequest,
  getAppointmentsForCalendarRequest,
  newAppointmentForCalendarRequest,
  NewAppointmentForCalendarRequestBody,
  updateAppointmentForCalendarRequest,
  UpdateAppointmentForCalendarRequestBody,
} from "@/lib/api";
import { Appointment } from "@/types";
import useSWR from "swr";

const useAppointments = (calendarId: string | undefined) => {
  const { data, error, mutate, isLoading } =
    useSWR<AppointmentsForCalendarResponseData>(
      !calendarId ? null : `appointments-${calendarId}`,
      () => getAppointmentsForCalendarRequest(calendarId!)
    );

  const addNewAppointment = async (
    body: NewAppointmentForCalendarRequestBody
  ) => {
    if (!calendarId) return;

    const optimisticAppointment = {
      ...body,
      id: `tmp-appointment-id-${Date.now()}`,
      calendarId,
    };

    await mutate(
      async () => {
        const addedAppointment = await newAppointmentForCalendarRequest(
          calendarId,
          body
        );
        return [...(data || []), addedAppointment];
      },
      {
        optimisticData: [...(data || []), optimisticAppointment],
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const updateAppointment = async (
    body: UpdateAppointmentForCalendarRequestBody,
    appointmentId: Appointment["id"]
  ) => {
    if (!calendarId) return;

    await mutate(
      async () => {
        await updateAppointmentForCalendarRequest(
          calendarId,
          appointmentId,
          body
        );
        return (data || []).map((d) =>
          d.id === appointmentId ? { ...d, ...body } : d
        );
      },
      {
        optimisticData: (data || []).map((d) =>
          d.id === appointmentId ? { ...d, ...body } : d
        ),
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  const deleteAppointment = async (appointmentId: Appointment["id"]) => {
    if (!calendarId) return;

    await mutate(
      async () => {
        await deleteAppointmentForCalendarRequest(calendarId, appointmentId);
        return (data || []).filter((d) => d.id !== appointmentId);
      },
      {
        optimisticData: (data || []).filter((d) => d.id !== appointmentId),
        rollbackOnError: true,
        populateCache: true,
        revalidate: false,
      }
    );
  };

  return {
    appointments: data,
    isLoading,
    error,
    addNewAppointment,
    updateAppointment,
    deleteAppointment,
  };
};

export { useAppointments };
