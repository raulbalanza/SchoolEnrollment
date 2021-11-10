package me.raulbalanza.tjv.school_enrollment.dao;

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
public class StudentTemporaryRepository implements CrudRepository<String, Student> {

    private ArrayList<Student> students;

    public StudentTemporaryRepository(){
        students = new ArrayList<Student>();

        // Dummy users for testing
        students.add(new Student("balanrau", "12345678A", "1234",
                "test@email.com", "Raul", "Balanza Garcia",
                LocalDate.of(2000, 1, 30), 3 ));

        students.add(new Student("testdummy", "00000000B", "1234",
                "test2@email.com", "Test", "User",
                LocalDate.of(2001, 5, 1), 4 ));
    }

    public void createOrUpdate(Student entity){
        this.deleteById(entity.getUsername());
        students.add(entity);
    }

    public Optional<Student> readById(String username){
        for (Student s: students){
            if (s.getUsername().equals(username)){
                return Optional.ofNullable(s);
            }
        }
        return Optional.empty();
    }

    public Collection<Student> readAll(){
        return students;
    }

    public void deleteById(String username){
        for (Student s: students){
            if (s.getUsername().equals(username)){
                students.remove(s);
                break;
            }
        }
    }

    public boolean exists(Student entity){
        for (Student s: students){
            if (s.getUsername().equals(entity.getUsername())){
                return true;
            }
        }
        return false;
    }

}
