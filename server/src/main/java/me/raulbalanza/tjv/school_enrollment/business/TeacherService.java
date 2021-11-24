package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.TeacherRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherService extends CrudService<String, Teacher, TeacherRepository> {

    protected TeacherService(TeacherRepository repository) {
        super(repository);
    }

    @Override
    protected boolean exists(Teacher entity) {
        return repository.existsById(entity.getUsername());
    }


}
