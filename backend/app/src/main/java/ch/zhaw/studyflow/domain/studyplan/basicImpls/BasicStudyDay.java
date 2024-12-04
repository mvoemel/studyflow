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
    private List<StudyAllocation> studyAllocations; //oder direkt appointments?

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
        return appointments;
    }

    @Override
    public void addAppointment(Appointment appointment) { //maybe unnecessary
        appointments.add(appointment);
    }

    @Override
    public void removeAppointment(Appointment appointment) { //maybe unnecessary
        appointments.remove(appointment);
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

    @Override
    public void calculateStudyAllocations() { 
        //TODO create time blocks of ?? hours/minutes based on available time in between start and end time
        //also take breaks and appointments into account
        //question: do we give the user the option to set the length of the study blocks? and length of breaks?
        //idea: otherwise figure out some way of adding optimal breaks (I have an idea)
    }





}
