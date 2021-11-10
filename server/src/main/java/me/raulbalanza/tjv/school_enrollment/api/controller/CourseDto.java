package me.raulbalanza.tjv.school_enrollment.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto {

    // These attributes are public because the DTO is just a transfer object. Wrong data will never get into the actual classes.
    @JsonView(Views.Basic.class)
    public String ID;

    @JsonView(Views.Basic.class)
    public String name;

    @JsonView(Views.Detailed.class)
    public int credits;

    @JsonView(Views.Detailed.class)
    public int year;

    @JsonView(Views.Detailed.class)
    public int capacity;

    @JsonView(Views.Detailed.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d.M.yyyy")
    public LocalDate enrollLimit;

    public CourseDto() { }

    public CourseDto(String ID, String name, int credits, int year, int capacity, LocalDate enrollLimit) {
        this.ID = ID;
        this.name = name;
        this.credits = credits;
        this.year = year;
        this.capacity = capacity;
        this.enrollLimit = enrollLimit;
    }
}
