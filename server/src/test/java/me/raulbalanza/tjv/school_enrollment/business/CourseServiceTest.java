package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.ClassIntervalRepository;
import me.raulbalanza.tjv.school_enrollment.dao.CourseRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private ClassIntervalRepository classIntervalRepository;

    @MockBean
    private TeacherService teacherService;

    Course c = new Course("BIE-TJV", "Java Technology", 6, 2021, 30, LocalDate.now().plusDays(1));
    Course c2 = new Course("BIE-AAG", "Automata and Grammars", 8, 2020, 24, LocalDate.now().plusDays(6));
    Student st = new Student("balanrau", "12345678A", "12345", "balanrau@fit.cvut.cz",
            "Raul", "Balanza Garcia", LocalDate.of(2000, 1, 30), 3);
    Student st2 = new Student("teststudent", "12345678C", "12345", "testing@fit.cvut.cz",
            "Test", "Student", LocalDate.of(2001, 6, 25), 2);
    Teacher t = new Teacher("guthondr", "12345678B", "123456", "ondrej.guth@fit.cvut.cz",
            "Ondrej", "Guth", LocalDate.of(1995, 4, 5), "Teacher");
    Teacher t2 = new Teacher("testteacher", "12345678D", "123456", "testing.teacher@fit.cvut.cz",
            "Test", "Teacher", LocalDate.of(1993, 3, 15), "Supervisor");

    @BeforeEach
    void setUp() {
        Mockito.when(courseRepository.findById(c.getID())).thenReturn(Optional.of(c));
        Mockito.when(courseRepository.findById(c2.getID())).thenReturn(Optional.empty());
        Mockito.when(courseRepository.existsById(c.getID())).thenReturn(true);
        Mockito.when(courseRepository.existsById(c2.getID())).thenReturn(false);
        Mockito.when(courseRepository.findAll()).thenReturn(List.of(c));

        t.getTeachingCourses().add(c);
        c.getTeachers().add(t);
        st.getEnrolledCourses().add(c);
        c.getStudents().add(st);
    }

    @Test
    void createNew() throws EntityStateException {

        this.courseService.create(c2);
        Mockito.verify(courseRepository, Mockito.atLeast(1)).save(c2);

    }

    @Test
    void createExisting() {

        assertThrows(EntityStateException.class, () -> this.courseService.create(c));
        Mockito.verify(courseRepository, Mockito.atMost(0)).save(c);

    }

    @Test
    void readByIdExists() throws UnknownEntityException {

        Course value = this.courseService.readById(c.getID());
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findById(c.getID());
        assertEquals(value, c);

    }

    @Test
    void readByIdDoesNotExist() {

        assertThrows(UnknownEntityException.class, () -> this.courseService.readById(c2.getID()));
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findById(c2.getID());

    }

    @Test
    void readAll() {

        Collection<Course> collection = this.courseService.readAll();
        assertEquals(1, collection.size());
        assertTrue(collection.contains(c));
        assertFalse(collection.contains(c2));
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findAll();

    }

    @Test
    void update() {
    }

    @Test
    void deleteByIdExists() throws UnknownEntityException {

        this.courseService.deleteById(c.getID());
        Mockito.verify(courseRepository, Mockito.atLeast(1)).deleteById(c.getID());

    }

    @Test
    void deleteByIdDoesNotExist() {

        assertThrows(UnknownEntityException.class, () -> this.courseService.deleteById(c2.getID()));
        Mockito.verify(courseRepository, Mockito.atMost(0)).deleteById(c2.getID());

    }

    @Test
    void exists() {

        assertTrue(this.courseService.exists(c));
        assertFalse(this.courseService.exists(c2));

    }

    @Test
    void readAllFiltered() {
    }

    @Test
    void addSchedule() {
    }

    @Test
    void removeSchedule() {
    }

    @Test
    void addTeacher() {
    }

    @Test
    void removeTeacher() {
    }
}