package me.raulbalanza.tjv.school_enrollment.dao;

import me.raulbalanza.tjv.school_enrollment.domain.Course;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    Course c = new Course("BIE-TJV", "Java Technology", 6, 2021, 10, LocalDate.now().plusDays(1));
    Course c2 = new Course("BIE-AAG", "Automata and Grammars", 8, 2021, 10, LocalDate.now().plusDays(2));
    Course c3 = new Course("BIE-AG1", "Algorithms and Graphs 1", 5, 2021, 10, LocalDate.now().plusDays(3));
    Course c4 = new Course("BIE-PA1", "Programming and Algorithmics 1", 3, 2021, 10, LocalDate.now().plusDays(4));
    Course c5 = new Course("NIE-MVI", "Computational Intelligence Methods", 9, 2021, 10, LocalDate.now().plusDays(5));
    Course c6 = new Course("NIE-SYP", "Parsing and Compilers", 2, 2021, 10, LocalDate.now().plusDays(6));

    Student st = new Student("balanrau", "12345678A", "12345", "balanrau@fit.cvut.cz",
            "Raul", "Balanza Garcia", LocalDate.of(2000, 1, 30), 3);
    Student st2 = new Student("teststudent", "12345678C", "12345", "testing@fit.cvut.cz",
            "Test", "Student", LocalDate.of(2001, 6, 25), 2);

    @BeforeEach
    void setUp() {
        // Insert data before each test
        this.studentRepository.save(st);
        this.studentRepository.save(st2);

        c2.getStudents().add(st);
        c4.getStudents().add(st);
        c5.getStudents().add(st2);

        for (Course c : List.of(c, c2, c3, c4, c5, c6)){
            this.courseRepository.save(c);
        }

    }

    @AfterEach
    void tearDown() {
        // Clean DB after each test
        courseRepository.deleteAll();
    }

    @Test
    void readAllFilterCreditsCapacity() {

        var res = this.courseRepository.readAllFilterCreditsCapacity(4, 10);

        for (Course c : List.of(c6)){ assertTrue(res.contains(c)); }
        for (Course c : List.of(c, c2, c3, c4, c5)){ assertFalse(res.contains(c)); }

        assertTrue(c2.getStudents().contains(st));
        assertTrue(c4.getStudents().contains(st));
        assertTrue(c5.getStudents().contains(st2));

    }

    @Test
    void readAllFilterCredits() {

        var res = this.courseRepository.readAllFilterCredits(6);
        for (Course c : List.of(c, c3, c4, c6)){ assertTrue(res.contains(c)); }
        for (Course c : List.of(c2, c5)){ assertFalse(res.contains(c)); }

    }

    @Test
    void readAllFilterCapacity() {

        var res = this.courseRepository.readAllFilterCapacity(10);
        for (Course c : List.of(c, c3, c6)){ assertTrue(res.contains(c)); }
        for (Course c : List.of(c2, c4, c5)){ assertFalse(res.contains(c)); }

        assertTrue(c2.getStudents().contains(st));
        assertTrue(c4.getStudents().contains(st));
        assertTrue(c5.getStudents().contains(st2));

    }
}