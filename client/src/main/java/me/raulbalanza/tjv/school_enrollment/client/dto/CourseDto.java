package me.raulbalanza.tjv.school_enrollment.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class CourseDto {

    // These attributes are public because the DTO is just a transfer object. Wrong data will never get into the actual classes.
    public String ID;
    public String name;
    public int credits;
    public int year;
    public int capacity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate enrollLimit;

    public CourseDto() { }

    public String errorMessage;

    public CourseDto(CourseDto other, String errorM) {
        if (other != null){
            this.ID = other.ID;
            this.name = other.name;
            this.credits = other.credits;
            this.year = other.year;
            this.capacity = other.capacity;
            this.enrollLimit = other.enrollLimit;
        }
        this.errorMessage = errorM;
    }

    public CourseDto(String ID, String name, int credits, int year, int capacity, LocalDate enrollLimit) {
        this.ID = ID;
        this.name = name;
        this.credits = credits;
        this.year = year;
        this.capacity = capacity;
        this.enrollLimit = enrollLimit;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

}
