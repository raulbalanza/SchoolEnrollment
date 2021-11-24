package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.StudentRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentService extends CrudService<String, Student, StudentRepository> {

    protected StudentService(StudentRepository repository) {
        super(repository);
    }

    @Override
    protected boolean exists(Student entity) {
        return repository.existsById(entity.getUsername());
    }


}
