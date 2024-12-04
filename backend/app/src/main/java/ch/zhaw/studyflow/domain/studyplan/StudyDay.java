package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalDate;
import java.util.List;

import ch.zhaw.studyflow.domain.calendar.Appointment;

public interface StudyDay {

    public LocalDate getDate();
    public void setDate(LocalDate date);
    public int getMinutes();
    public void calculateMinutes();
    public List<Appointment> getAppointments();
    public void addAppointment(Appointment appointment);
    public void removeAppointment(Appointment appointment);
    public List<StudyAllocation> getStudyAllocations();
    public void addStudyAllocation(StudyAllocation studyAllocation);
    public void removeStudyAllocation(StudyAllocation studyAllocation);
    public void calculateStudyAllocations();
}
