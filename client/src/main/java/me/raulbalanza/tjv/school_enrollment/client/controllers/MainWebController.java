package me.raulbalanza.tjv.school_enrollment.client.controllers;

import me.raulbalanza.tjv.school_enrollment.client.dto.CourseDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.FilterDto;
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
public class MainWebController {

    private final CourseClient courseClient;
    private final TeacherClient teacherClient;

    public MainWebController(CourseClient courseClient, TeacherClient teacherClient){
        this.courseClient = courseClient;
        this.teacherClient = teacherClient;
    }

    @GetMapping("/")
    public String loadMainPage(Model model){
        model.addAttribute("courses", courseClient.readAllCourses(new FilterDto()));
        model.addAttribute("teachers", teacherClient.readAllCourses());
        //model.addAttribute("students", studentClient.readAllCourses());
        return "index";
    }

    @GetMapping("/home")
    public String loadMainPageAlt(Model model){
        return loadMainPage(model);
    }

}
