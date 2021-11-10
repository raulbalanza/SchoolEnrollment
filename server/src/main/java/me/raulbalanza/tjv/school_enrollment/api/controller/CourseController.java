package me.raulbalanza.tjv.school_enrollment.api.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.annotation.JsonView;
import me.raulbalanza.tjv.school_enrollment.api.converter.CourseConverter;
import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.CourseService;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;

@RestController
class CourseController {

    private final CourseService courseService;

    CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @JsonView(Views.Basic.class)
    @GetMapping("/courses")
    Collection<CourseDto> getAll(@RequestParam(defaultValue = "-1") String max_credits, @RequestParam(defaultValue = "0") String min_free_capacity) {
        // This will return all courses taking into account restrictions of the query parameters
        return CourseConverter.fromCollection(this.courseService.readAll());
    }

    @PostMapping("/courses")
    @JsonView(Views.Detailed.class)
    CourseDto createCourse(@RequestBody CourseDto course) throws EntityStateException {
        Course model = CourseConverter.toModel(course);
        this.courseService.create(model);
        return CourseConverter.fromModel(model);
    }

    @JsonView(Views.Detailed.class)
    @GetMapping("/courses/{id}")
    CourseDto getOne(@PathVariable String id) throws UnknownEntityException {
        Course c = this.courseService.readById(id);
        return CourseConverter.fromModel(c);
    }

    @JsonView(Views.Detailed.class)
    @PutMapping("/courses/{id}")
    CourseDto updateCourse(@RequestBody CourseDto courseDto, @PathVariable String id) throws UnknownEntityException {
        this.courseService.readById(id);                // Reading the course just to make sure that it exists
        Course c = CourseConverter.toModel(courseDto);
        this.courseService.update(c);
        return courseDto;
    }

    @JsonView(Views.Detailed.class)
    @DeleteMapping("/courses/{id}")
    void deleteCourse(@PathVariable String id) throws UnknownEntityException {
        this.courseService.deleteById(id);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/courses/{id}/schedule")
    ClassInterval [] getSchedule(@PathVariable String id) throws UnknownEntityException {
        Course c = this.courseService.readById(id);
        return c.getSchedule();
    }

    @PostMapping("/courses/{id}/schedule")
    ClassInterval [] addSchedule(@RequestBody ClassIntervalDto interval, @PathVariable String id) throws UnknownEntityException {
        ClassInterval ci = CourseConverter.intervalToModel(interval);
        if (this.courseService.addSchedule(id, ci)){
            Course c = this.courseService.readById(id);
            return c.getSchedule();
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The proposed schedule overlaps with the previous timetable.");
        }
    }

    @DeleteMapping("/courses/{id}/schedule")
    void deleteSchedule(@RequestBody ClassIntervalDto interval, @PathVariable String id) throws UnknownEntityException {
        ClassInterval ci = CourseConverter.intervalToModel(interval);
        if (!this.courseService.removeSchedule(id, ci)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified timetable could not be found.");
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/courses/{id}/teachers")
    Collection<TeacherDto> getTeachersInCourse(@PathVariable String id) throws EntityStateException {
        // This will return the list of the teachers that teach the course
        return Collections.EMPTY_LIST;
    }

    @PostMapping("/courses/{id}/teachers/{username}")
    Collection<CourseDto> teachCourse(@PathVariable String username, @PathVariable String id) throws EntityStateException {
        // This will add the teacher to the course and then return the list of the taught courses
        return Collections.EMPTY_LIST;
    }

    @DeleteMapping("/courses/{id}/teachers/{username}")
    Collection<CourseDto> removeCourse(@PathVariable String username, @PathVariable String id) throws EntityStateException {
        // This will remove the teacher from the course and then return the list of the taught courses
        return Collections.EMPTY_LIST;
    }

}