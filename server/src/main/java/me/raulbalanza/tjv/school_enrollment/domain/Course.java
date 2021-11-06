package me.raulbalanza.tjv.school_enrollment.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Course {

    private final String ID;
    private String name;
    private double credits;
    private int year;
    private int capacity;
    private LocalDateTime enrollLimit;
    private List<ClassInterval> schedule;
    // teacher list

    public Course(String ID, String name, double credits, int year, int capacity, LocalDateTime enrollLimit) {
        this.ID = ID;
        this.name = name;
        this.credits = credits;
        this.year = year;
        this.capacity = capacity;
        this.enrollLimit = enrollLimit;
        this.schedule = new ArrayList<ClassInterval>();
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalDateTime getEnrollLimit() {
        return enrollLimit;
    }

    public void setEnrollLimit(LocalDateTime enrollLimit) {
        this.enrollLimit = enrollLimit;
    }

    public boolean addSchedule(ClassInterval ci){
        return ci != null && this.schedule.add(ci);
    }

    public boolean removeSchedule(ClassInterval ci){
        return ci != null && this.schedule.remove(ci);
    }

    public ClassInterval [] getSchedule(){
        ClassInterval[] res = new ClassInterval[this.schedule.size()];
        int i = 0;
        for (ClassInterval ci : this.schedule){
            res[i++] = ci;
        }
        return res;
    }
}
