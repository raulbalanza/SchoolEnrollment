package me.raulbalanza.tjv.school_enrollment.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Course {

    @Id
    private String ID;

    private String name;
    private int credits;
    private int year;
    private int capacity;
    private LocalDate enrollLimit;

    @OneToMany(mappedBy = "course")
    private Collection<ClassInterval> schedule = new ArrayList<ClassInterval>();

    @ManyToMany
    private Collection<Teacher> teachers = new ArrayList<Teacher>();

    @ManyToMany
    private Collection<Student> students = new ArrayList<Student>();

    public Course(String ID, String name, int credits, int year, int capacity, LocalDate enrollLimit) {
        this.ID = Objects.requireNonNull(ID);
        this.name = name;
        this.credits = credits;
        this.year = year;
        this.capacity = capacity;
        this.enrollLimit = enrollLimit;
    }

    public Course(){ }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setSchedule(List<ClassInterval> schedule) {
        this.schedule = schedule;
    }

    public void setTeachers(Collection<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Collection<Teacher> getTeachers() {
        return teachers;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public Collection<Student> getStudents() {
        return students;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
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

    public LocalDate getEnrollLimit() {
        return enrollLimit;
    }

    public void setEnrollLimit(LocalDate enrollLimit) {
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
