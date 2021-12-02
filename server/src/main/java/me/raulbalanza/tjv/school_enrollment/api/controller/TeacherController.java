package me.raulbalanza.tjv.school_enrollment.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import me.raulbalanza.tjv.school_enrollment.api.converter.StudentConverter;
import me.raulbalanza.tjv.school_enrollment.api.converter.TeacherConverter;
import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.StudentService;
import me.raulbalanza.tjv.school_enrollment.business.TeacherService;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Collections;

@RestController
class TeacherController {

    private final TeacherService teacherService;

    TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @JsonView(Views.Basic.class)
    @GetMapping("/teachers")
    Collection<TeacherDto> getAll() {
        return TeacherConverter.fromCollection(this.teacherService.readAll());
    }

    @PostMapping("/teachers")
    @JsonView(Views.Detailed.class)
    TeacherDto createTeacher(@RequestBody TeacherDto teacher) throws EntityStateException {
        Teacher model = TeacherConverter.toModel(teacher);
        this.teacherService.create(model);
        return TeacherConverter.fromModel(model);
    }

    @JsonView(Views.Detailed.class)
    @GetMapping("/teachers/{username}")
    TeacherDto getOne(@PathVariable String username) throws UnknownEntityException {
        Teacher t = this.teacherService.readById(username);
        return TeacherConverter.fromModel(t);
    }

    @JsonView(Views.Detailed.class)
    @PutMapping("/teachers/{username}")
    TeacherDto updateUser(@RequestBody TeacherDto teacherDto, @PathVariable String username) throws UnknownEntityException, InvalidParameterException {
        Teacher t = TeacherConverter.toModel(teacherDto);
        if (t.getUsername() == null || !t.getUsername().equals(username))
            throw new InvalidParameterException("The username provided in the URI does not match the one in the request body");
        this.teacherService.update(t);
        return TeacherConverter.fromModel(this.teacherService.readById(t.getUsername()));
    }

    @JsonView(Views.Detailed.class)
    @DeleteMapping("/teachers/{username}")
    void deleteUser(@PathVariable String username) throws UnknownEntityException {
        this.teacherService.deleteById(username);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

}