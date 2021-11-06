package me.raulbalanza.tjv.school_enrollment.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class Teacher extends User {

    // missing list of subjects that the teacher is teaching
    protected String rank;

    public Teacher(String username, String password, String email, String name, String surnames, String ID, LocalDateTime birthDate, String rank) {
        super(username, password, email, name, surnames, ID, birthDate);
        this.rank = rank;
    }

    public Collection<Course> getSubjects(){
        return Collections.EMPTY_LIST;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
