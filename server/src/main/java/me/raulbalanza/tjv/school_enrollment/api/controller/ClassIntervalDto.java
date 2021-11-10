package me.raulbalanza.tjv.school_enrollment.api.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassIntervalDto {

    public DayOfWeek day;
    public LocalTime start;

    public LocalTime end;

    public ClassIntervalDto() {}

    public ClassIntervalDto(DayOfWeek day, LocalTime start, LocalTime end) {
        this.day = day;
        this.start = start;
        this.end = end;
    }

}
