package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.ClassIntervalRepository;
import me.raulbalanza.tjv.school_enrollment.dao.CourseRepository;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.security.InvalidParameterException;
import java.util.Collection;

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

    @Override
    @Transactional
    public void update(Course entity) throws UnknownEntityException {
        var current = this.readById(entity.getID());

        // Update attributes: name, credits, year, capacity, enrollLimit
        if (entity.getCapacity() >= current.getStudents().size()){
            current.setCapacity(entity.getCapacity());
        } else throw new InvalidParameterException("The new capacity is lower than the current number of enrolled students.");
        if (entity.getName() != null) current.setName(entity.getName());
        if (entity.getCredits() != 0) current.setCredits(entity.getCredits());
        if (entity.getYear() != 0) current.setYear(entity.getYear());
        if (entity.getEnrollLimit() != null) current.setEnrollLimit(entity.getEnrollLimit());

        super.update(current);
    }

    public Collection<Course> readAllFiltered(String max_credits, String min_free_capacity) throws InvalidParameterException {

        try {
            int maxCreds = Integer.parseInt(max_credits);
            int minCap = Integer.parseInt(min_free_capacity);

            if (maxCreds < -1 || minCap < 0)
                throw new InvalidParameterException("The provided values must be positive integer numbers.");

            if (maxCreds == -1 && minCap == 0) return this.readAll();
            else if (maxCreds != -1 && minCap == 0) return this.repository.readAllFilterCredits(maxCreds);
            else if (maxCreds == -1 && minCap != 0) return this.repository.readAllFilterCapacity(minCap);
            else return this.repository.readAllFilterCreditsCapacity(maxCreds, minCap);

        } catch (NumberFormatException e) {
            throw new InvalidParameterException("The provided values must be positive integer numbers.");
        }

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
