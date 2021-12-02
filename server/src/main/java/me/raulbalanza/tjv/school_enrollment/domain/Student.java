package me.raulbalanza.tjv.school_enrollment.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Student {

    @Id
    protected String username;

    @NotNull
    protected String password;
    @NotNull
    protected String ID;
    @NotNull
    protected String email;
    @NotNull
    protected String name;
    @NotNull
    protected String surnames;
    protected LocalDate birthDate;
    protected LocalDateTime registeredAt;
    @NotNull
    @Positive
    protected int currentYear;

    @ManyToMany(mappedBy = "students")
    private Collection<Course> enrolledCourses = new ArrayList<Course>();;

    public Student(String username, String ID, String password, String email, String name, String surnames, LocalDate birthDate, int currentYear) {
        //this.username = Objects.requireNonNull(username);
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surnames = surnames;
        this.ID = ID;
        this.birthDate = birthDate;
        this.registeredAt = LocalDateTime.now();
        this.currentYear = currentYear;
    }

    public Student() {}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnrolledCourses(Collection<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public Collection<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public Collection<Course> getSubjects(){
        return enrolledCourses;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Student s){
            return this.username.equals(s.username);
        } else return false;
    }

    @Override
    public String toString() {
        return "Student " + this.username + " with ID: " + this.ID +
                "\n\t - Name: " + this.name +
                "\n\t - Surnames: " + this.surnames +
                "\n\t - Email: " + this.email +
                "\n\t - Role: " + this.email +
                "\n\t - Birthdate: " + ((this.birthDate == null) ? "-" : this.birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE)) +
                "\n\t - Registered at: " + this.registeredAt.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
