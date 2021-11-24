package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.ClassIntervalRepository;
import me.raulbalanza.tjv.school_enrollment.dao.CourseRepository;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@Component
public class CourseService extends CrudService<String, Course, CourseRepository> {

    private final ClassIntervalRepository classIntervalRepository;
    private final TeacherService teacherService;

    protected CourseService(CourseRepository courseRepository, ClassIntervalRepository classIntervalRepository, TeacherService teacherService) {
        super(courseRepository);
        this.classIntervalRepository = classIntervalRepository;
        this.teacherService = teacherService;
    }

    protected boolean exists(Course entity) {
        return repository.existsById(entity.getID());
    }

    @Transactional
    public void addSchedule(String id, ClassInterval newCi) throws EntityStateException, UnknownEntityException {

        if (id == null || newCi == null)
            throw new UnknownEntityException("One of the required entities was not provided.");

        var c = readById(id);

        for (ClassInterval ci : c.getSchedule()){
            if (ci.overlaps(newCi)){
                throw new EntityStateException("The proposed schedule overlaps with the existing timetable.");
            }
        }

        newCi.setCourse(c);
        ClassInterval added = classIntervalRepository.save(newCi);
        c.addSchedule(added); // No other schedule overlaps, can add
        repository.save(c);

    }

    @Transactional
    public void removeSchedule(String id, ClassInterval oldCi) throws UnknownEntityException {

        if (oldCi == null || id == null)
            throw new UnknownEntityException("One of the required entities was not provided.");

        var c = readById(id);

        for (ClassInterval ci : c.getSchedule()){
            if (ci.equals(oldCi)){
                this.classIntervalRepository.delete(ci); // Found the schedule, removing it
                return;
            }
        }

        throw new UnknownEntityException("The specified timetable could not be found.");

    }

    @Transactional
    public void addTeacher(String courseId, String teacherId) throws EntityStateException, UnknownEntityException {

        var course = this.readById(courseId);
        var teacher = this.teacherService.readById(teacherId);

        if (!course.getTeachers().contains(teacher)){
            course.getTeachers().add(teacher);
            this.repository.save(course);
            return;
        }

        throw new EntityStateException("The teacher with that username was already teaching the course.");

    }

    @Transactional
    public void removeTeacher(String courseId, String teacherId) throws EntityStateException, UnknownEntityException {

        var course = this.readById(courseId);
        var teacher = this.teacherService.readById(teacherId);

        if (course.getTeachers().contains(teacher)){
            course.getTeachers().remove(teacher);
            this.repository.save(course);
            return;
        }

        throw new EntityStateException("The teacher with that username was not teaching the course.");

    }

}
