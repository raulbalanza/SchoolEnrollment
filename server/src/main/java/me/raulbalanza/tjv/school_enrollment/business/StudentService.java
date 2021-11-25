package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.StudentRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
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
    @Override
    public void deleteById(String username) {

        var student = this.repository.getById(username);

        for (Course c: student.getEnrolledCourses()){
            try { this.abandonCourse(username, c.getID()); }
            catch (EntityStateException | UnknownEntityException e) { return; }
        }

        repository.deleteById(username);
    }

    @Transactional
    public void enrollCourse(String studentID, String courseID) throws EntityStateException, UnknownEntityException {

        var course = this.courseService.readById(courseID);
        var student = this.readById(studentID);

        if (course.getStudents().size() >= course.getCapacity()){
            throw new EntityStateException("The course " + course.getID() + " is full. Cannot add new students.");
        }

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
