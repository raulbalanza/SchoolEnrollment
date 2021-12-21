package me.raulbalanza.tjv.school_enrollment.client.controllers;

import me.raulbalanza.tjv.school_enrollment.client.dto.ClassIntervalDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.CourseDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.FilterDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.StudentDto;
import me.raulbalanza.tjv.school_enrollment.client.web_clients.CourseClient;
import me.raulbalanza.tjv.school_enrollment.client.web_clients.StudentClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Controller
public class StudentWebController {

    private final StudentClient studentClient;
    private final CourseClient courseClient;

    private final String CONFLICT_MSG = "A student with the specified username already exists.";
    private final String BAD_REQUEST_MSG = "Some values are not correct, review them and try again.";
    private final String GENERAL_ERROR_MSG = "An error has occurred, check the provided values.";
    private final String NOT_FOUND_MSG = "That student or course does not exist, check the provided username.";
    private final String ALREADY_ENROLLED_MSG = "That student was already enrolled in that course.";
    private final String NOT_ENROLLED_MSG = "That student was not enrolled in that course.";

    public StudentWebController(StudentClient studentClient, CourseClient courseClient){
        this.studentClient = studentClient;
        this.courseClient = courseClient;
    }

    @GetMapping("/students")
    public String getAll(Model model){
        model.addAttribute("students", studentClient.readAllStudents());
        return "students";
    }

    @GetMapping("/students/create")
    public String addStudentRender(Model model){
        model.addAttribute("view", "create");
        model.addAttribute("studentDto", new StudentDto());
        return "editStudent";
    }

    @PostMapping("/students/create")
    public String addStudentSubmit(Model model, @ModelAttribute StudentDto studentDto){
        model.addAttribute("view", "create");
        model.addAttribute(
                "studentDto",
                studentClient.create(studentDto)
                        .onErrorReturn(WebClientResponseException.Conflict.class, new StudentDto(studentDto, CONFLICT_MSG))
                        .onErrorReturn(WebClientResponseException.BadRequest.class, new StudentDto(studentDto, BAD_REQUEST_MSG))
                        .onErrorReturn(new StudentDto(studentDto, GENERAL_ERROR_MSG))

        );
        return "editStudent";
    }

    @GetMapping("/students/delete/{username}")
    public String deleteStudent(Model model, @PathVariable String username){
        model.addAttribute("username", username);
        model.addAttribute("response", studentClient.delete(username).onErrorReturn("-"));
        return "deleteStudent";
    }

    @GetMapping("/students/edit/{username}")
    public String editStudentRender(Model model, @PathVariable String username){
        model.addAttribute("view", "edit");
        model.addAttribute("studentDto", studentClient.getOne(username).onErrorReturn(new StudentDto(null, NOT_FOUND_MSG)));
        return "editStudent";
    }

    @PostMapping("/students/edit")
    public String editStudentSubmit(Model model, @ModelAttribute StudentDto studentDto){
        model.addAttribute("view", "edit");
        model.addAttribute(
                "studentDto",
                studentClient.update(studentDto)
                        .onErrorReturn(WebClientResponseException.Conflict.class, new StudentDto(studentDto, CONFLICT_MSG))
                        .onErrorReturn(WebClientResponseException.BadRequest.class, new StudentDto(studentDto, BAD_REQUEST_MSG))
                        .onErrorReturn(new StudentDto(studentDto, GENERAL_ERROR_MSG))

        );
        return "editStudent";
    }

    @GetMapping("/students/courses/{username}")
    public String getStudentCourses(Model model, @PathVariable String username){
        model.addAttribute("studentID", username);
        model.addAttribute("courses", studentClient.getStudentCourses(username)
                .onErrorReturn(new CourseDto(null, NOT_FOUND_MSG))
        );
        return "studentCourses";
    }

    @GetMapping("/students/courses/{username}/create")
    public String enrollStudentInCourseRender(Model model, @PathVariable String username){
        model.addAttribute("studentID", username);
        model.addAttribute("courses", courseClient.readAllCourses(new FilterDto())
                .onErrorReturn(new CourseDto(null, NOT_FOUND_MSG))
        );
        return "addStudentCourse";
    }

    @GetMapping("/students/courses/{username}/create/{id}")
    public String enrollStudentInCourseRender(Model model, @PathVariable String username, @PathVariable String id){
        model.addAttribute("studentID", username);
        model.addAttribute("courses", studentClient.enrollStudentInCourse(username, id)
                .onErrorReturn(WebClientResponseException.Conflict.class, new CourseDto(null, ALREADY_ENROLLED_MSG))
                .onErrorReturn(new CourseDto(null, NOT_FOUND_MSG))
        );
        return "studentCourses";
    }

    @GetMapping("/students/courses/{username}/delete/{id}")
    public String removeEnrollmentFromCourse(Model model, @PathVariable String username, @PathVariable String id){
        model.addAttribute("studentID", username);
        model.addAttribute("courses", studentClient.removeEnrollmentFromCourse(username, id)
                .onErrorReturn(WebClientResponseException.Conflict.class, new CourseDto(null, NOT_ENROLLED_MSG))
                .onErrorReturn(new CourseDto(null, NOT_FOUND_MSG))
        );
        return "studentCourses";
    }

}
