package me.raulbalanza.tjv.school_enrollment.client.controllers;

import me.raulbalanza.tjv.school_enrollment.client.web_clients.CourseClient;
import me.raulbalanza.tjv.school_enrollment.client.dto.CourseDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.FilterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Controller
public class CourseWebController {

    private final CourseClient courseClient;

    private final String CONFLICT_MSG = "A course with the specified ID already exists.";
    private final String BAD_REQUEST_MSG = "Some values are not correct, review them and try again.";
    private final String GENERAL_ERROR_MSG = "An error has occurred, check the provided values.";
    private final String NOT_FOUND_MSG = "That course does not exist, check the provided ID.";
    private final String NOT_VALID_FILTER = "The specified filter values are not valid.";

    public CourseWebController(CourseClient courseClient){
        this.courseClient = courseClient;
    }

    @GetMapping("/courses")
    public String getAll(Model model){
        model.addAttribute("filterDto", new FilterDto());
        model.addAttribute("courses", courseClient.readAllCourses(new FilterDto()));
        return "courses";
    }

    @PostMapping("/courses/filter")
    public String getAll(Model model, @ModelAttribute FilterDto filterDto){

        System.out.println(filterDto.max_credits);
        System.out.println(filterDto.min_free_capacity);

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

}
