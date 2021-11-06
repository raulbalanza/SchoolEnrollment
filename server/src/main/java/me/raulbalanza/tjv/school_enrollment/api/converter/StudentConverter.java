package me.raulbalanza.tjv.school_enrollment.api.converter;

import me.raulbalanza.tjv.school_enrollment.api.controller.StudentDto;
import me.raulbalanza.tjv.school_enrollment.domain.Student;

import java.util.ArrayList;
import java.util.Collection;

public class StudentConverter {

    public static Student toModel(StudentDto studentDto) {
        return new Student(studentDto.username, studentDto.ID, studentDto.password, studentDto.email,
                studentDto.name, studentDto.surnames, studentDto.birthDate, studentDto.currentYear);
    }

    public static StudentDto fromModel(Student student) {
        return new StudentDto(student.getUsername(), student.getID(), student.getPassword(), student.getEmail(),
                student.getName(), student.getSurnames(), student.getBirthDate(), student.getCurrentYear());
    }

    public static Collection<StudentDto> fromCollection(Collection<Student> students){
        Collection<StudentDto> res = new ArrayList<StudentDto>();
        for (Student s: students){
            res.add(fromModel(s));
        }
        return res;
    }

}
