package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.CrudRepository;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;

public class CourseService extends CrudService<String, Course> {

    protected CourseService(CrudRepository<String, Course> repository) {
        super(repository);
    }

    public boolean addSchedule(String id, ClassInterval newCi) throws UnknownEntityException {

        if (newCi == null) return false;

        Course c = readById(id);

        for (ClassInterval ci : c.getSchedule()){
            if (ci.overlaps(newCi)){
                return false; // Found an overlapping schedule, cannot add
            }
        }

        return c.addSchedule(newCi); // No other schedule overlaps, can add

    }

    public boolean removeSchedule(String id, ClassInterval oldCi) throws UnknownEntityException {

        if (oldCi == null || id == null) return false;

        Course c = readById(id);

        for (ClassInterval ci : c.getSchedule()){
            if (ci.equals(oldCi)){
                return c.removeSchedule(oldCi); // Found the schedule, removing it
            }
        }

        return false; // That schedule did not belong to the subject, cannot remove

    }

}
