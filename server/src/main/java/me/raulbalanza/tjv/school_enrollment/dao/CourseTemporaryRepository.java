package me.raulbalanza.tjv.school_enrollment.dao;

import me.raulbalanza.tjv.school_enrollment.domain.Course;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Component
public class CourseTemporaryRepository implements CrudRepository<String, Course> {

    private ArrayList<Course> courses;

    public CourseTemporaryRepository(){
        courses = new ArrayList<Course>();

        // Dummy courses for testing
        courses.add(new Course("BIE-TJV", "Java Technology", 4, 2021, 30, LocalDate.now()));

        courses.add(new Course("BIE-PA1", "Programming and Algorithmics 1", 7, 2021, 30, LocalDate.now().minusDays(5)));
    }

    public void createOrUpdate(Course entity){
        this.deleteById(entity.getID());
        courses.add(entity);
    }

    public Optional<Course> readById(String id){
        for (Course c: courses){
            if (c.getID().equals(id)){
                return Optional.ofNullable(c);
            }
        }
        return Optional.empty();
    }

    public Collection<Course> readAll(){
        return courses;
    }

    public void deleteById(String id){
        for (Course c: courses){
            if (c.getID().equals(id)){
                courses.remove(c);
                break;
            }
        }
    }

    public boolean exists(Course entity){
        for (Course c: courses){
            if (c.getID().equals(entity.getID())){
                return true;
            }
        }
        return false;
    }

}
