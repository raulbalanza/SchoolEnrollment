package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.CrudRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentService extends CrudService<String, Student> {

    protected StudentService(CrudRepository<String, Student> repository) {
        super(repository);
    }

}
