package me.raulbalanza.tjv.school_enrollment.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClassInterval {

    private DayOfWeek day;
    private LocalTime start;
    private LocalTime end;

    public ClassInterval(DayOfWeek d, int hStart, int mStart, int hEnd, int mEnd){
        this.day = d;
        this.start = LocalTime.of(hStart, mStart);
        this.end = LocalTime.of(hEnd, mEnd);
    }

    public boolean overlaps(ClassInterval ci){

        if (this.day.equals(ci.day)){

            // (StartA < EndB) and (EndA > StartB)

            return this.start.toSecondOfDay() < ci.end.toSecondOfDay()
                    && this.end.toSecondOfDay() > ci.start.toSecondOfDay();

        } else return false;

    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ClassInterval c){
            return day.equals(c.day) && this.start.equals(c.start) && this.end.equals(c.end);
        } else return false;
    }

    @Override
    public String toString(){
        return day.name() + " - " + this.start.format(DateTimeFormatter.ISO_LOCAL_TIME) +
                " to " + this.end.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

}
