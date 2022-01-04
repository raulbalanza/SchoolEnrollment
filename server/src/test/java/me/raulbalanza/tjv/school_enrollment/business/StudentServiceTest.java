package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.StudentRepository;
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
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private CourseService courseService;

    Course c = new Course("BIE-TJV", "Java Technology", 6, 2021, 30, LocalDate.now().plusDays(1));
    Course c2 = new Course("BIE-AAG", "Automata and Grammars", 8, 2020, 24, LocalDate.now().plusDays(6));
    Student st = new Student("balanrau", "12345678A", "12345", "balanrau@fit.cvut.cz",
            "Raul", "Balanza Garcia", LocalDate.of(2000, 1, 30), 3);
    Student st2 = new Student("teststudent", "12345678C", "12345", "testing@fit.cvut.cz",
            "Test", "Student", LocalDate.of(2001, 6, 25), 2);

    @BeforeEach
    void setUp() throws UnknownEntityException {
        Mockito.when(studentRepository.findById(st.getUsername())).thenReturn(Optional.of(st));
        Mockito.when(studentRepository.findById(st2.getUsername())).thenReturn(Optional.empty());
        Mockito.when(studentRepository.findAll()).thenReturn(List.of(st));
        Mockito.when(studentRepository.existsById(st.getUsername())).thenReturn(true);
        Mockito.when(studentRepository.existsById(st2.getUsername())).thenReturn(false);
        Mockito.when(studentRepository.save(st2)).thenReturn(st2);
        Mockito.when(courseService.readById(c.getID())).thenReturn(c);
        Mockito.when(courseService.readById(c2.getID())).thenReturn(c2);

        st.getEnrolledCourses().add(c);
        c.getStudents().add(st);
    }

    @Test
    void createNew() throws EntityStateException {

        this.studentService.create(st2);
        Mockito.verify(studentRepository, Mockito.atLeast(1)).save(st2);

    }

    @Test
    void createExisting() {

        assertThrows(EntityStateException.class, () -> this.studentService.create(st));
        Mockito.verify(studentRepository, Mockito.never()).save(st);

    }

    @Test
    void readByIdExists() throws UnknownEntityException {

        Student value = this.studentService.readById(st.getUsername());
        Mockito.verify(studentRepository, Mockito.atLeast(1)).findById(st.getUsername());
        assertEquals(value, st);

    }

    @Test
    void readByIdDoesNotExist() {

        assertThrows(UnknownEntityException.class, () -> this.studentService.readById(st2.getUsername()));
        Mockito.verify(studentRepository, Mockito.atLeast(1)).findById(st2.getUsername());

    }

    @Test
    void readAll() {

        Collection<Student> collection = this.studentService.readAll();
        assertEquals(1, collection.size());
        assertTrue(collection.contains(st));
        assertFalse(collection.contains(st2));
        Mockito.verify(studentRepository, Mockito.atLeast(1)).findAll();

    }

    @Test
    void updateExists() throws UnknownEntityException {

        Student st_changed = new Student("balanrau", "12345678C", "abcde", "balanrau@fit.cvut.cz",
                "Raul", "Balanza Garcia", LocalDate.of(2000, 1, 30), 3);

        this.studentService.update(st_changed);
        Mockito.verify(studentRepository, Mockito.atLeast(1)).save(st_changed);

    }

    @Test
    void updateExistsNullPassword() throws UnknownEntityException {

        Student st_changed = new Student("balanrau", "12345678C", null, "balanrau@fit.cvut.cz",
                "Raul", "Balanza Garcia", LocalDate.of(2000, 1, 30), 3);

        this.studentService.update(st_changed);
        assertTrue(st_changed.getPassword().equals(st.getPassword()));
        Mockito.verify(studentRepository, Mockito.atLeast(1)).save(st_changed);

    }

    @Test
    void updateDoesNotExist() {

        Student st2_changed = new Student("teststudent", "12345678Z", "12345", "testing@fit.cvut.cz",
                "Test", "Student Changed", LocalDate.of(2001, 6, 25), 2);

        assertThrows(UnknownEntityException.class, () -> this.studentService.update(st2_changed));
        Mockito.verify(studentRepository, Mockito.atLeast(0)).save(st2_changed);

    }

    @Test
    void deleteByIdExists() throws UnknownEntityException, EntityStateException {

        this.studentService.deleteById(st.getUsername());
        Mockito.verify(studentRepository, Mockito.atLeast(1)).findById(st.getUsername());
        Mockito.verify(studentRepository, Mockito.atLeast(1)).deleteById(st.getUsername());
        assertFalse(c.getStudents().contains(st));

    }

    @Test
    void deleteByIdDoesNotExist() {

        assertThrows(UnknownEntityException.class, () -> this.studentService.deleteById(st2.getUsername()));
        Mockito.verify(studentRepository, Mockito.atLeast(1)).findById(st2.getUsername());
        Mockito.verify(studentRepository, Mockito.never()).deleteById(st2.getUsername());

    }

    @Test
    void exists() {

        assertTrue(this.studentService.exists(st));
        assertFalse(this.studentService.exists(st2));

    }

    @Test
    void enrollCourseNotEnrolled() throws UnknownEntityException, EntityStateException {

        this.studentService.enrollCourse(st.getUsername(), c2.getID());
        Mockito.verify(this.courseService, Mockito.atLeast(1)).readById(c2.getID());
        Mockito.verify(this.courseService, Mockito.atLeast(1)).update(c2);
        assertEquals(c2.getStudents().size(), 1);

    }

    @Test
    void enrollCourseWhichIsFull() throws UnknownEntityException {

        c2.setCapacity(0);

        assertThrows(EntityStateException.class, () -> this.studentService.enrollCourse(st.getUsername(), c2.getID()));
        Mockito.verify(this.courseService, Mockito.atLeast(1)).readById(c2.getID());
        Mockito.verify(this.courseService, Mockito.never()).update(c2);

    }

    @Test
    void enrollCourseAlreadyEnrolled() throws UnknownEntityException {

        assertThrows(EntityStateException.class, () -> this.studentService.enrollCourse(st.getUsername(), c.getID()));
        Mockito.verify(this.courseService, Mockito.atLeast(1)).readById(c.getID());
        Mockito.verify(this.courseService, Mockito.never()).update(c);

    }

    @Test
    void abandonCourseNotEnrolled() throws UnknownEntityException {

        assertThrows(EntityStateException.class, () -> this.studentService.abandonCourse(st.getUsername(), c2.getID()));
        Mockito.verify(this.courseService, Mockito.atLeast(1)).readById(c2.getID());
        Mockito.verify(this.courseService, Mockito.never()).update(c2);

    }

    @Test
    void abandonCourseEnrolled() throws UnknownEntityException, EntityStateException {

        this.studentService.abandonCourse(st.getUsername(), c.getID());
        Mockito.verify(this.courseService, Mockito.atLeast(1)).readById(c.getID());
        Mockito.verify(this.courseService, Mockito.atLeast(1)).update(c);
        assertEquals(c.getStudents().size(), 0);

    }

}