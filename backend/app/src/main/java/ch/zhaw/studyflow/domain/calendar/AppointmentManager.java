package ch.zhaw.studyflow.domain.calendar;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentManager {
    void create(Appointment appointment);

    Appointment read(long calendarId, long id);

    List<Appointment> readAllBy(long calendarId, LocalDate from, LocalDate to);

    void delete(long id);

    void update(Appointment appointment);
}
