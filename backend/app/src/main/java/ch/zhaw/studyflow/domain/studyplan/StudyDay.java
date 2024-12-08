package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalDate;
import java.util.List;

import ch.zhaw.studyflow.domain.calendar.Appointment;

public interface StudyDay extends Comparable<StudyDay> {
    LocalDate getDate();
    void setDate(LocalDate date);
    long getMinutes();
    void calculateMinutes();
    List<Appointment> getAppointments();
    void addAppointment(Appointment appointment);
    void removeAppointment(Appointment appointment);
    List<StudyAllocation> getStudyAllocations();
    void addStudyAllocation(StudyAllocation studyAllocation);
    void removeStudyAllocation(StudyAllocation studyAllocation);
    void calculateStudyAllocations();

    @Override
    int compareTo(StudyDay other);
}
