package me.raulbalanza.tjv.school_enrollment.api.converter;

import me.raulbalanza.tjv.school_enrollment.api.controller.ClassIntervalDto;
import me.raulbalanza.tjv.school_enrollment.api.controller.CourseDto;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;

import java.util.ArrayList;
import java.util.Collection;

public class CourseConverter {

    public static Course toModel(CourseDto courseDto) {
        return new Course(courseDto.ID, courseDto.name, courseDto.credits, courseDto.year, courseDto.capacity, courseDto.enrollLimit);
    }

    public static CourseDto fromModel(Course course) {
        return new CourseDto(course.getID(), course.getName(), course.getCredits(), course.getYear(), course.getCapacity(), course.getEnrollLimit());
    }

    public static Collection<CourseDto> fromCollection(Collection<Course> courses){
        Collection<CourseDto> res = new ArrayList<CourseDto>();
        for (Course c: courses){
            res.add(fromModel(c));
        }
        return res;
    }

    public static ClassIntervalDto intervalFromModel(ClassInterval classInterval) {
        return new ClassIntervalDto(classInterval.getDay(), classInterval.getStart(), classInterval.getEnd());
    }

    public static ClassInterval intervalToModel(ClassIntervalDto classIntervalDto) {
        return new ClassInterval(classIntervalDto.day, classIntervalDto.start.getHour(), classIntervalDto.start.getMinute(),
                classIntervalDto.end.getHour(), classIntervalDto.end.getMinute());
    }

}
