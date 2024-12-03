package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.studyplan.StudyAllocation;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;

public class BasicStudyDay implements StudyDay {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int minutes;
    private List<Appointment> appointments;
    private List<StudyAllocation> studyAllocations;

    public BasicStudyDay(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    @Override 
    public void calculateMinutes() {
        minutes = 0;
        for (StudyAllocation studyAllocation : studyAllocations) {
            minutes += studyAllocation.getMinutes();
        }
    }

    @Override
    public List<Appointment> getAppointments() {
        return null;
    }

    @Override
    public void addAppointment(Appointment appointment) {
    }

    @Override
    public void removeAppointment(Appointment appointment) {
    }

    @Override
    public List<StudyAllocation> getStudyAllocations() {
        return studyAllocations;
    }

    @Override
    public void addStudyAllocation(StudyAllocation studyAllocation) {
        studyAllocations.add(studyAllocation);
    }

    @Override
    public void removeStudyAllocation(StudyAllocation studyAllocation) {
        studyAllocations.remove(studyAllocation);
    }





}
