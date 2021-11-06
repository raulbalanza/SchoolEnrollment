package me.raulbalanza.tjv.school_enrollment.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class Student extends User {

    // missing subjects where the student is enrolled
    protected int currentYear;

    public Student(String username, String ID, String password, String email, String name, String surnames, LocalDateTime birthDate, int currentYear) {
        super(username, password, email, name, surnames, ID, birthDate);
        this.currentYear = currentYear;
    }

    public Collection<Course> getSubjects(){
        return Collections.EMPTY_LIST;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }
}
