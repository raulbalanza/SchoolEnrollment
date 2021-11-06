package me.raulbalanza.tjv.school_enrollment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import me.raulbalanza.tjv.school_enrollment.api.converter.StudentConverter;
import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.StudentService;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
class StudentController {

    private final StudentService studentService;

    StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @JsonView(Views.Basic.class)
    @GetMapping("/students")
    Collection<StudentDto> getAll() {
        return StudentConverter.fromCollection(this.studentService.readAll());
    }

    @PostMapping("/students")
    @JsonView(Views.Detailed.class)
    StudentDto createUser(@RequestBody StudentDto student) throws EntityStateException {
        Student model = StudentConverter.toModel(student);
        this.studentService.create(model);
        return StudentConverter.fromModel(model);
    }

    @JsonView(Views.Detailed.class)
    @GetMapping("/students/{username}")
    StudentDto getOne(@PathVariable String username) throws UnknownEntityException {
        Student s = this.studentService.readById(username);
        return StudentConverter.fromModel(s);
    }

    @JsonView(Views.Detailed.class)
    @PutMapping("/students/{username}")
    StudentDto updateUser(@RequestBody StudentDto studentDto, @PathVariable String username) throws UnknownEntityException {
        Student st = StudentConverter.toModel(studentDto);
        this.studentService.update(st);
        return studentDto;
    }

    @JsonView(Views.Detailed.class)
    @DeleteMapping("/students/{username}")
    void deleteUser(@PathVariable String username) throws UnknownEntityException {
        this.studentService.deleteById(username);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

}