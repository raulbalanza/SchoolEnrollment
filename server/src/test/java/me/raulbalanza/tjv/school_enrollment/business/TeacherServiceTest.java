package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.CourseRepository;
import me.raulbalanza.tjv.school_enrollment.dao.TeacherRepository;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherServiceTest {

    @Autowired // insert instance by dependency injection
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private CourseRepository courseRepository;

    Course c = new Course("BIE-TJV", "Java Technology", 6, 2021, 30, LocalDate.now().plusDays(1));
    Teacher t = new Teacher("guthondr", "12345678B", "123456", "ondrej.guth@fit.cvut.cz",
            "Ondrej", "Guth", LocalDate.of(1995, 4, 5), "Teacher");
    Teacher t2 = new Teacher("testteacher", "12345678D", "123456", "testing.teacher@fit.cvut.cz",
            "Test", "Teacher", LocalDate.of(1993, 3, 15), "Supervisor");

    @BeforeEach
    void setUp() {
        Mockito.when(teacherRepository.findById(t.getUsername())).thenReturn(Optional.of(t));
        Mockito.when(teacherRepository.findById(t2.getUsername())).thenReturn(Optional.empty());
        Mockito.when(teacherRepository.findAll()).thenReturn(List.of(t));
        Mockito.when(teacherRepository.existsById(t.getUsername())).thenReturn(true);
        Mockito.when(teacherRepository.existsById(t2.getUsername())).thenReturn(false);
        Mockito.when(teacherRepository.save(t2)).thenReturn(t2);

        t.getTeachingCourses().add(c);
        c.getTeachers().add(t);
    }

    @Test
    void createNew() throws EntityStateException {

        this.teacherService.create(t2);
        Mockito.verify(teacherRepository, Mockito.atLeast(1)).save(t2);

    }

    @Test
    void createExisting() {

        assertThrows(EntityStateException.class, () -> this.teacherService.create(t));
        Mockito.verify(teacherRepository, Mockito.atMost(0)).save(t);

    }

    @Test
    void readByIdExists() throws UnknownEntityException {

        Teacher value = this.teacherService.readById(t.getUsername());
        Mockito.verify(teacherRepository, Mockito.atLeast(1)).findById(t.getUsername());
        assertEquals(value, t);

    }

    @Test
    void readByIdDoesNotExist() {

        assertThrows(UnknownEntityException.class, () -> this.teacherService.readById(t2.getUsername()));
        Mockito.verify(teacherRepository, Mockito.atLeast(1)).findById(t2.getUsername());

    }

    @Test
    void readAll() {

        Collection<Teacher> collection = this.teacherService.readAll();
        assertEquals(1, collection.size());
        assertTrue(collection.contains(t));
        assertFalse(collection.contains(t2));
        Mockito.verify(teacherRepository, Mockito.atLeast(1)).findAll();

    }

    @Test
    void updateExists() throws UnknownEntityException {

        Teacher t_changed = new Teacher("guthondr", "12345678B", "123456", "new_email@fit.cvut.cz",
                "Ondrej", "Guth", LocalDate.of(1995, 4, 5), "Teacher");

        this.teacherService.update(t_changed);
        Mockito.verify(teacherRepository, Mockito.atLeast(1)).save(t_changed);

    }

    @Test
    void updateDoesNotExist() {

        Teacher t2_changed = new Teacher("testteacher", "12345678D", "123456", "testing.teacher@fit.cvut.cz",
                "Test", "Teacher", LocalDate.of(1993, 3, 15), "Supervisor");

        assertThrows(UnknownEntityException.class, () -> this.teacherService.update(t2_changed));
        Mockito.verify(teacherRepository, Mockito.atLeast(0)).save(t2_changed);

    }

    @Test
    void deleteByIdExists() throws UnknownEntityException {

        this.teacherService.deleteById(t.getUsername());
        Mockito.verify(teacherRepository, Mockito.atLeast(1)).findById(t.getUsername());
        Mockito.verify(teacherRepository, Mockito.atLeast(1)).deleteById(t.getUsername());
        Mockito.verify(courseRepository, Mockito.atLeast(1)).save(c);
        assertFalse(c.getTeachers().contains(t));

    }

    @Test
    void deleteByIdDoesNotExist() {

        assertThrows(UnknownEntityException.class, () -> this.teacherService.deleteById(t2.getUsername()));
        Mockito.verify(teacherRepository, Mockito.atLeast(1)).findById(t2.getUsername());
        Mockito.verify(teacherRepository, Mockito.atMost(0)).deleteById(t2.getUsername());

    }

    @Test
    void exists() {

        assertTrue(this.teacherService.exists(t));
        assertFalse(this.teacherService.exists(t2));

    }
}