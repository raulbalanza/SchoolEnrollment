package me.raulbalanza.tjv.school_enrollment.api.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.fasterxml.jackson.annotation.JsonView;
import me.raulbalanza.tjv.school_enrollment.api.converter.CourseConverter;
import me.raulbalanza.tjv.school_enrollment.api.converter.TeacherConverter;
import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.CourseService;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import org.aspectj.apache.bcel.classfile.Unknown;
import org.hibernate.event.internal.EntityState;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
    @PutMapping("/courses") // Update using the ID attribute of JSON, id cannot be changed
    CourseDto updateCourse(@RequestBody CourseDto courseDto) throws UnknownEntityException {
        Course c = CourseConverter.toModel(courseDto);
        this.courseService.update(c);
        return CourseConverter.fromModel(this.courseService.readById(c.getID()));
    }

    @JsonView(Views.Detailed.class)
    @DeleteMapping("/courses/{id}")
    void deleteCourse(@PathVariable String id) throws UnknownEntityException {
        this.courseService.readById(id); // Read first to check if it exists
        this.courseService.deleteById(id);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/courses/{id}/schedule")
    ClassInterval [] getSchedule(@PathVariable String id) throws UnknownEntityException {
        Course c = this.courseService.readById(id);
        return c.getSchedule();
    }

    @PostMapping("/courses/{id}/schedule")
    ClassInterval [] addSchedule(@RequestBody ClassInterval ci, @PathVariable String id) throws EntityStateException, UnknownEntityException {
        this.courseService.addSchedule(id, ci);
        Course c = this.courseService.readById(id);
        return c.getSchedule();
    }

    @DeleteMapping("/courses/{id}/schedule")
    void deleteSchedule(@RequestBody ClassInterval ci, @PathVariable String id) throws UnknownEntityException {
        this.courseService.removeSchedule(id, ci);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    @JsonView(Views.Basic.class)
    @GetMapping("/courses/{id}/teachers")
    Collection<TeacherDto> getTeachersInCourse(@PathVariable String id) throws UnknownEntityException {
        Course c = this.courseService.readById(id);
        return TeacherConverter.fromCollection(c.getTeachers());
    }

    @JsonView(Views.Basic.class)
    @PostMapping("/courses/{id}/teachers/{username}")
    Collection<TeacherDto> teachCourse(@PathVariable String username, @PathVariable String id) throws UnknownEntityException, EntityStateException {
        this.courseService.addTeacher(id, username);
        return TeacherConverter.fromCollection(this.courseService.readById(id).getTeachers());
    }

    @JsonView(Views.Basic.class)
    @DeleteMapping("/courses/{id}/teachers/{username}")
    Collection<TeacherDto> removeCourse(@PathVariable String username, @PathVariable String id) throws UnknownEntityException, EntityStateException {
        this.courseService.removeTeacher(id, username);
        return TeacherConverter.fromCollection(this.courseService.readById(id).getTeachers());
    }

}