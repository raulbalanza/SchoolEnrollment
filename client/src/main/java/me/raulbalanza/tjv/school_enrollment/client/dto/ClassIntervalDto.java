package me.raulbalanza.tjv.school_enrollment.client.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

public class ClassIntervalDto {

    public DayOfWeek day;

    @DateTimeFormat(pattern = "HH:mm")
    public LocalTime start;

    @DateTimeFormat(pattern = "HH:mm")
    public LocalTime finish;

    public String errorMessage;

    public ClassIntervalDto(ClassIntervalDto other, String errorM) {
        if (other != null){
            this.day = other.day;
            this.finish = other.finish;
            this.start = other.start;
        }
        this.errorMessage = errorM;
    }

    public ClassIntervalDto(DayOfWeek d, LocalTime start, LocalTime end){
        this.day = Objects.requireNonNull(d);
        this.start = start;
        this.finish = end;
    }

    public ClassIntervalDto() { }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public void setFinish(LocalTime end) {
        this.finish = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getFinish() {
        return finish;
    }

}
