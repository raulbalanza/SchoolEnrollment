package me.raulbalanza.tjv.school_enrollment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
public class ClassInterval {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_1")
    @SequenceGenerator(name = "seq_1", sequenceName = "seq_1", allocationSize = 1)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private DayOfWeek day;
    private LocalTime start;
    private LocalTime finish;

    public ClassInterval(DayOfWeek d, int hStart, int mStart, int hEnd, int mEnd){
        this.day = Objects.requireNonNull(d);
        this.start = LocalTime.of(hStart, mStart);
        this.finish = LocalTime.of(hEnd, mEnd);
    }

    public ClassInterval() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

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

    public boolean overlaps(ClassInterval ci){

        if (this.day.equals(ci.day)){

            // (StartA < EndB) and (EndA > StartB)

            return this.start.toSecondOfDay() < ci.finish.toSecondOfDay()
                    && this.finish.toSecondOfDay() > ci.start.toSecondOfDay();

        } else return false;

    }

    @Override
    public int hashCode() {
        return day.hashCode() + start.hashCode() + finish.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ClassInterval c){
            return day.equals(c.day) && this.start.equals(c.start) && this.finish.equals(c.finish);
        } else return false;
    }

    @Override
    public String toString(){
        return day.name() + " - " + this.start.format(DateTimeFormatter.ISO_LOCAL_TIME) +
                " to " + this.finish.format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

}
