package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.StudentRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class StudentService extends CrudService<String, Student, StudentRepository> {

    private final CourseService courseService;

    protected StudentService(StudentRepository repository, CourseService courseService) {
        super(repository);
        this.courseService = courseService;
    }

    @Override
    protected boolean exists(Student entity) {
        return repository.existsById(entity.getUsername());
    }

    @Transactional
    public void enrollCourse(String studentID, String courseID) throws EntityStateException, UnknownEntityException {

        var course = this.courseService.readById(courseID);
        var student = this.readById(studentID);

        if (!course.getStudents().contains(student)){
            course.getStudents().add(student);
            this.courseService.update(course);
            return;
        }

        throw new EntityStateException("The student with that username was already enrolled in the course.");

    }

    @Transactional
    public void abandonCourse(String studentID, String courseID) throws EntityStateException, UnknownEntityException {

        var course = this.courseService.readById(courseID);
        var student = this.readById(studentID);

        if (course.getStudents().contains(student)){
            course.getStudents().remove(student);
            this.courseService.update(course);
            return;
        }

        throw new EntityStateException("The student with that username was not enrolled in the course.");

    }

}
