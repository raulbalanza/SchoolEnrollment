package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.CourseRepository;
import me.raulbalanza.tjv.school_enrollment.dao.TeacherRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class TeacherService extends CrudService<String, Teacher, TeacherRepository> {

    private final CourseRepository courseRepository;

    protected TeacherService(TeacherRepository repository, CourseRepository courseRepository) {
        super(repository);
        this.courseRepository = courseRepository;
    }

    @Override
    public void update(Teacher entity) throws UnknownEntityException {

        if (entity.getPassword() == null) {
            var teach = this.readById(entity.getUsername());
            entity.setPassword(teach.getPassword());
        }

        super.update(entity);

    }

    @Transactional
    @Override
    public void deleteById(String username) throws UnknownEntityException {

        var teacher = this.readById(username);

        for (Course c: teacher.getTeachingCourses()){
            c.getTeachers().remove(teacher);
            this.courseRepository.save(c);
        }

        super.deleteById(username);

    }

    @Override
    protected boolean exists(Teacher entity) {
        return repository.existsById(entity.getUsername());
    }

}
