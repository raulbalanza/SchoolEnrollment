package me.raulbalanza.tjv.school_enrollment.api.converter;

import me.raulbalanza.tjv.school_enrollment.api.controller.StudentDto;
import me.raulbalanza.tjv.school_enrollment.api.controller.TeacherDto;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;

import java.util.ArrayList;
import java.util.Collection;

public class TeacherConverter {

    public static Teacher toModel(TeacherDto teacherDto) {
        return new Teacher(teacherDto.username, teacherDto.ID, teacherDto.password, teacherDto.email,
                teacherDto.name, teacherDto.surnames, teacherDto.birthDate, teacherDto.rank);
    }

    public static TeacherDto fromModel(Teacher teacher) {
        return new TeacherDto(teacher.getUsername(), teacher.getID(), teacher.getPassword(), teacher.getEmail(),
                teacher.getName(), teacher.getSurnames(), teacher.getBirthDate(), teacher.getRank());
    }

    public static Collection<TeacherDto> fromCollection(Collection<Teacher> teachers){
        Collection<TeacherDto> res = new ArrayList<TeacherDto>();
        for (Teacher t: teachers){
            res.add(fromModel(t));
        }
        return res;
    }

}
