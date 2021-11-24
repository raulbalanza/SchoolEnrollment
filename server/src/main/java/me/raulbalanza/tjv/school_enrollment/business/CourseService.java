package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.CourseRepository;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseService extends CrudService<String, Course, CourseRepository> {

    protected CourseService(CourseRepository repository) {
        super(repository);
    }

    protected boolean exists(Course entity) {
        return repository.existsById(entity.getID());
    }

    public boolean addSchedule(String id, ClassInterval newCi) throws UnknownEntityException {

        if (id == null || newCi == null) return false;

        var c = readById(id);
        if (c.isEmpty()) return false;

        for (ClassInterval ci : c.get().getSchedule()){
            if (ci.overlaps(newCi)){
                return false; // Found an overlapping schedule, cannot add
            }
        }

        return c.get().addSchedule(newCi); // No other schedule overlaps, can add

    }

    public boolean removeSchedule(String id, ClassInterval oldCi) throws UnknownEntityException {

        if (oldCi == null || id == null) return false;

        var c = readById(id);
        if (c.isEmpty()) return false;

        for (ClassInterval ci : c.get().getSchedule()){
            if (ci.equals(oldCi)){
                return c.get().removeSchedule(oldCi); // Found the schedule, removing it
            }
        }

        return false; // That schedule did not belong to the subject, cannot remove

    }

}
