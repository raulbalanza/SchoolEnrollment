package me.raulbalanza.tjv.school_enrollment.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
public class Course {

    @Id
    private String ID;

    @NotNull(message = "name")
    private String name;
    @NotNull(message = "credits")
    private int credits;
    @NotNull(message = "year")
    private int year;
    @NotNull(message = "capacity")
    private int capacity;
    @NotNull(message = "enrollLimit")
    private LocalDate enrollLimit;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "course")
    private Collection<ClassInterval> schedule = new ArrayList<ClassInterval>();

    @ManyToMany
    @JoinTable(
            name = "teaching_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Collection<Teacher> teachers = new ArrayList<Teacher>();

    @ManyToMany
    @JoinTable(
            name = "enrolled_courses",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Collection<Student> students = new ArrayList<Student>();

    public Course(String ID, String name, int credits, int year, int capacity, LocalDate enrollLimit) {
        //this.ID = Objects.requireNonNull(ID);
        this.ID = ID;
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

    @Override
    public int hashCode() {
        return this.ID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        Course c = null;
        if (!(obj instanceof Course)) return false;
        else c = (Course) obj;

        return this.ID.equals(c.ID) && this.name.equals(c.name);

    }

    @Override
    public String toString() {
        return "Course: " + this.name + " (" + this.ID + ")" +
                "\n\t - Credits: " + this.name +
                "\n\t - Capacity: " + this.capacity +
                "\n\t - Enrolled students: " + this.students.size() +
                "\n\t - Enroll limit: " + ((this.enrollLimit == null) ? "-" : this.enrollLimit.format(DateTimeFormatter.ISO_LOCAL_DATE)) +
                "\n\t - Year: " + this.year;
    }
}
