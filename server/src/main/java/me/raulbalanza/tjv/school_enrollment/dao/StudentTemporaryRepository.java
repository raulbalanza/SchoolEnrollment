package me.raulbalanza.tjv.school_enrollment.dao;

import me.raulbalanza.tjv.school_enrollment.domain.Student;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

@Component
public class StudentTemporaryRepository implements CrudRepository<String, Student> {

    private HashMap<String, Student> students;

    public StudentTemporaryRepository(){
        students = new HashMap<String, Student>();

        // Dummy users for testing
        students.put("balanrau", new Student("balanrau", "12345678A", "1234",
                "test@email.com", "Raul", "Balanza Garcia",
                LocalDateTime.of(2000, 1, 30, 0, 0), 3 ));

        students.put("testdummy", new Student("testdummy", "00000000B", "1234",
                "test2@email.com", "Test", "User",
                LocalDateTime.of(2001, 5, 1, 0, 0), 4 ));
    }

    public void createOrUpdate(Student entity){
        students.put(entity.getUsername(), entity);
    }

    public Optional<Student> readById(String id){
        if (students.containsKey(id)){
            return Optional.ofNullable(students.get(id));
        }
        return Optional.empty();
    }

    public Collection<Student> readAll(){
        return students.values();
    }

    public void deleteById(String id){
        students.remove(id);
    }

    public boolean exists(Student entity){
        return students.containsValue(entity);
    }

}
