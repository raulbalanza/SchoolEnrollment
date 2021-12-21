package me.raulbalanza.tjv.school_enrollment.client.controllers;

import me.raulbalanza.tjv.school_enrollment.client.dto.ClassIntervalDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.TeacherDto;
import me.raulbalanza.tjv.school_enrollment.client.web_clients.CourseClient;
import me.raulbalanza.tjv.school_enrollment.client.dto.CourseDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.FilterDto;
import me.raulbalanza.tjv.school_enrollment.client.web_clients.TeacherClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Controller
public class CourseWebController {

    private final CourseClient courseClient;
    private final TeacherClient teacherClient;

    private final String CONFLICT_MSG = "A course with the specified ID already exists.";
    private final String BAD_REQUEST_MSG = "Some values are not correct, review them and try again.";
    private final String GENERAL_ERROR_MSG = "An error has occurred, check the provided values.";
    private final String NOT_FOUND_MSG = "That course does not exist, check the provided ID.";
    private final String NOT_VALID_FILTER = "The specified filter values are not valid.";
    private final String INTERVAL_CONFLICT = "The specified schedule overlaps with an existing one.";
    private final String ALREADY_TEACHING_MSG = "That teacher was already teaching that course.";
    private final String NOT_TEACHING_MSG = "That teacher was not teaching that course.";

    public CourseWebController(CourseClient courseClient, TeacherClient teacherClient){
        this.courseClient = courseClient;
        this.teacherClient = teacherClient;
    }

    @GetMapping("/courses")
    public String getAll(Model model){
        model.addAttribute("filterDto", new FilterDto());
        model.addAttribute("courses", courseClient.readAllCourses(new FilterDto()));
        return "courses";
    }

    @GetMapping("/courses/schedule/{id}")
    public String getCourseSchedule(Model model, @PathVariable String id){
        model.addAttribute("courseID", id);
        model.addAttribute("intervals", courseClient.getCourseSchedule(id).onErrorReturn(new ClassIntervalDto(null, NOT_FOUND_MSG)));
        return "courseSchedule";
    }

    @GetMapping("/courses/schedule/{id}/delete")
    public String deleteCourseSchedule(Model model, @PathVariable String id, @RequestParam String day, @RequestParam String start, @RequestParam String finish){
        LocalTime st = LocalTime.parse(start);
        LocalTime fi = LocalTime.parse(finish);
        DayOfWeek dOw = DayOfWeek.valueOf(day);

        model.addAttribute("id", id);
        model.addAttribute("start", start);
        model.addAttribute("finish", finish);
        model.addAttribute("day", day);
        model.addAttribute("response",
                courseClient.deleteCourseSchedule(id, new ClassIntervalDto(dOw, st, fi)).onErrorReturn("-")
        );

        return "deleteCourseSchedule";
    }

    @GetMapping("/courses/schedule/{id}/create")
    public String addCourseScheduleRender(Model model, @PathVariable String id){
        model.addAttribute("courseID", id);
        model.addAttribute("classIntervalDto", new ClassIntervalDto());
        return "addCourseSchedule";
    }

    @PostMapping("/courses/schedule/{id}/create")
    public String addCourseScheduleSubmit(Model model, @ModelAttribute ClassIntervalDto classIntervalDto, @PathVariable String id){
        model.addAttribute("courseID", id);
        model.addAttribute("classIntervalDto",
                courseClient.addCourseSchedule(id, classIntervalDto)
                        .onErrorReturn(WebClientResponseException.Conflict.class, new ClassIntervalDto(classIntervalDto, INTERVAL_CONFLICT))
                        .onErrorReturn(WebClientResponseException.BadRequest.class, new ClassIntervalDto(classIntervalDto, BAD_REQUEST_MSG))
                        .onErrorReturn(new ClassIntervalDto(classIntervalDto, GENERAL_ERROR_MSG)));
        return "addCourseSchedule";
    }

    @PostMapping("/courses/filter")
    public String getAll(Model model, @ModelAttribute FilterDto filterDto){
        model.addAttribute("filterDto", filterDto);
        model.addAttribute("courses",
                courseClient.readAllCourses(filterDto)
                .onErrorReturn(new CourseDto(null, NOT_VALID_FILTER))
        );
        return "courses";
    }

    @GetMapping("/courses/create")
    public String addCourseRender(Model model){
        model.addAttribute("view", "create");
        model.addAttribute("courseDto", new CourseDto());
        return "editCourse";
    }

    @PostMapping("/courses/create")
    public String addCourseSubmit(Model model, @ModelAttribute CourseDto courseDto){
        model.addAttribute("view", "create");
        model.addAttribute(
                "courseDto",
                courseClient.create(courseDto)
                        .onErrorReturn(WebClientResponseException.Conflict.class, new CourseDto(courseDto, CONFLICT_MSG))
                        .onErrorReturn(WebClientResponseException.BadRequest.class, new CourseDto(courseDto, BAD_REQUEST_MSG))
                        .onErrorReturn(new CourseDto(courseDto, GENERAL_ERROR_MSG))

        );
        return "editCourse";
    }

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(Model model, @PathVariable String id){
        model.addAttribute("id", id);
        model.addAttribute("response", courseClient.delete(id).onErrorReturn("-"));
        return "deleteCourse";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourseRender(Model model, @PathVariable String id){
        model.addAttribute("view", "edit");
        model.addAttribute("courseDto", courseClient.getOne(id).onErrorReturn(new CourseDto(null, NOT_FOUND_MSG)));
        return "editCourse";
    }

    @PostMapping("/courses/edit")
    public String editCourseSubmit(Model model, @ModelAttribute CourseDto courseDto){
        model.addAttribute("view", "edit");
        model.addAttribute(
                "courseDto",
                courseClient.update(courseDto)
                        .onErrorReturn(WebClientResponseException.Conflict.class, new CourseDto(courseDto, CONFLICT_MSG))
                        .onErrorReturn(WebClientResponseException.BadRequest.class, new CourseDto(courseDto, BAD_REQUEST_MSG))
                        .onErrorReturn(new CourseDto(courseDto, GENERAL_ERROR_MSG))

        );
        return "editCourse";
    }

    @GetMapping("/courses/teachers/{id}")
    public String getCourseTeachers(Model model, @PathVariable String id){
        model.addAttribute("courseID", id);
        model.addAttribute("teachers", courseClient.getCourseTeachers(id)
                .onErrorReturn(new TeacherDto(null, NOT_FOUND_MSG))
        );
        return "courseTeachers";
    }

    @GetMapping("/courses/teachers/{id}/delete/{username}")
    public String removeTeacherFromCourse(Model model, @PathVariable String id, @PathVariable String username){
        model.addAttribute("courseID", id);
        model.addAttribute("teachers", courseClient.removeTeacherFromCourse(id, username)
                .onErrorReturn(WebClientResponseException.Conflict.class, new TeacherDto(null, NOT_TEACHING_MSG))
                .onErrorReturn(new TeacherDto(null, NOT_FOUND_MSG))
        );
        return "courseTeachers";
    }

    @GetMapping("/courses/teachers/{id}/create")
    public String addTeacherToCourse(Model model, @PathVariable String id){
        model.addAttribute("courseID", id);
        model.addAttribute("teachers", teacherClient.readAllTeachers()
                .onErrorReturn(new TeacherDto(null, NOT_FOUND_MSG))
        );
        return "addCourseTeacher";
    }

    @GetMapping("/courses/teachers/{id}/create/{username}")
    public String addTeacherToCourse(Model model, @PathVariable String id, @PathVariable String username){
        model.addAttribute("courseID", id);
        model.addAttribute("teachers", courseClient.addTeacherToCourse(id, username)
                .onErrorReturn(WebClientResponseException.Conflict.class, new TeacherDto(null, ALREADY_TEACHING_MSG))
                .onErrorReturn(new TeacherDto(null, NOT_FOUND_MSG))
        );
        return "courseTeachers";
    }

}
