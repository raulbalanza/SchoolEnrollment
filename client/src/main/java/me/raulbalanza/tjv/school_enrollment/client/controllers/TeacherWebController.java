package me.raulbalanza.tjv.school_enrollment.client.controllers;

import me.raulbalanza.tjv.school_enrollment.client.dto.CourseDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.FilterDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.TeacherDto;
import me.raulbalanza.tjv.school_enrollment.client.web_clients.CourseClient;
import me.raulbalanza.tjv.school_enrollment.client.web_clients.TeacherClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Controller
public class TeacherWebController {

    private final TeacherClient teacherClient;

    private final String CONFLICT_MSG = "A teacher with the specified username already exists.";
    private final String BAD_REQUEST_MSG = "Some values are not correct, review them and try again.";
    private final String GENERAL_ERROR_MSG = "An error has occurred, check the provided values.";
    private final String NOT_FOUND_MSG = "That teacher does not exist, check the provided username.";

    public TeacherWebController(TeacherClient teacherClient){
        this.teacherClient = teacherClient;
    }

    @GetMapping("/teachers")
    public String getAll(Model model){
        model.addAttribute("teachers", teacherClient.readAllCourses());
        return "teachers";
    }

    @GetMapping("/teachers/create")
    public String addTeacherRender(Model model){
        model.addAttribute("view", "create");
        model.addAttribute("teacherDto", new TeacherDto());
        return "editTeacher";
    }

    @PostMapping("/teachers/create")
    public String addTeacherSubmit(Model model, @ModelAttribute TeacherDto teacherDto){
        model.addAttribute("view", "create");
        model.addAttribute(
                "teacherDto",
                teacherClient.create(teacherDto)
                        .onErrorReturn(WebClientResponseException.Conflict.class, new TeacherDto(teacherDto, CONFLICT_MSG))
                        .onErrorReturn(WebClientResponseException.BadRequest.class, new TeacherDto(teacherDto, BAD_REQUEST_MSG))
                        .onErrorReturn(new TeacherDto(teacherDto, GENERAL_ERROR_MSG))

        );
        return "editTeacher";
    }

    @GetMapping("/teachers/delete/{username}")
    public String deleteTeacher(Model model, @PathVariable String username){
        model.addAttribute("username", username);
        model.addAttribute("response", teacherClient.delete(username).onErrorReturn("-"));
        return "deleteTeacher";
    }

    @GetMapping("/teachers/edit/{username}")
    public String editCourseRender(Model model, @PathVariable String username){
        model.addAttribute("view", "edit");
        model.addAttribute("teacherDto", teacherClient.getOne(username).onErrorReturn(new TeacherDto(null, NOT_FOUND_MSG)));
        return "editTeacher";
    }

    @PostMapping("/teachers/edit")
    public String editCourseSubmit(Model model, @ModelAttribute TeacherDto teacherDto){
        model.addAttribute("view", "edit");
        model.addAttribute(
                "teacherDto",
                teacherClient.update(teacherDto)
                        .onErrorReturn(WebClientResponseException.Conflict.class, new TeacherDto(teacherDto, CONFLICT_MSG))
                        .onErrorReturn(WebClientResponseException.BadRequest.class, new TeacherDto(teacherDto, BAD_REQUEST_MSG))
                        .onErrorReturn(new TeacherDto(teacherDto, GENERAL_ERROR_MSG))

        );
        return "editTeacher";
    }

}
