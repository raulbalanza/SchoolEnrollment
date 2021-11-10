package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.CrudRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherService extends CrudService<String, Teacher> {

    protected TeacherService(CrudRepository<String, Teacher> repository) {
        super(repository);
    }

}
