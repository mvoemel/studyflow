package ch.zhaw.studyflow.domain.studyplan;

public interface StudyDayManager {
    public void createStudyDay(long studyplanId, long moduleId, long calendarId);
    public void updateStudyDay(long id, long studyplanId, long moduleId, long calendarId);
    public void deleteStudyDay(long id);
    

    //these managers (just like the studyallocationmanager) are also mostly for "future use" and to show that we have thought about how to implement the studyplan
}
