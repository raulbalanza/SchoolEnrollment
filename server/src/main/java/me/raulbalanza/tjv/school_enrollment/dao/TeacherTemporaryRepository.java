package me.raulbalanza.tjv.school_enrollment.dao;

import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component
public class TeacherTemporaryRepository implements CrudRepository<String, Teacher> {

    private ArrayList<Teacher> teachers;

    public TeacherTemporaryRepository(){
        teachers = new ArrayList<Teacher>();

        // Dummy teachers for testing
        teachers.add(new Teacher("guthondr", "12345678A", "1234",
                "test@email.com", "Ondrej", "Guth",
                LocalDate.of(2000, 1, 30), "Department leader" ));

        teachers.add(new Teacher("testteacher", "00000000B", "1234",
                "test2@email.com", "Test", "User",
                LocalDate.of(2001, 5, 1), "Lecturer" ));
    }

    public void createOrUpdate(Teacher entity){
        this.deleteById(entity.getUsername());
        teachers.add(entity);
    }

    public Optional<Teacher> readById(String username){
        for (Teacher t: teachers){
            if (t.getUsername().equals(username)){
                return Optional.ofNullable(t);
            }
        }
        return Optional.empty();
    }

    public Collection<Teacher> readAll(){
        return teachers;
    }

    public void deleteById(String username){
        for (Teacher t: teachers){
            if (t.getUsername().equals(username)){
                teachers.remove(t);
                break;
            }
        }
    }

    public boolean exists(Teacher entity){
        for (Teacher t: teachers){
            if (t.getUsername().equals(entity.getUsername())){
                return true;
            }
        }
        return false;
    }

}
