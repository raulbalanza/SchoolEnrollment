package me.raulbalanza.tjv.school_enrollment.business;

import me.raulbalanza.tjv.school_enrollment.dao.ClassIntervalRepository;
import me.raulbalanza.tjv.school_enrollment.dao.CourseRepository;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

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
    Teacher t = new Teacher("guthondr", "12345678B", "123456", "ondrej.guth@fit.cvut.cz",
            "Ondrej", "Guth", LocalDate.of(1995, 4, 5), "Teacher");
    Teacher t2 = new Teacher("testteacher", "12345678D", "123456", "testing.teacher@fit.cvut.cz",
            "Test", "Teacher", LocalDate.of(1993, 3, 15), "Supervisor");
    Teacher t3 = new Teacher("testteacher2", "12345678E", "163456", "testing.teacher.2@fit.cvut.cz",
            "Test", "Professor", LocalDate.of(1997, 5, 15), "Assistant");
    ClassInterval ci = new ClassInterval(DayOfWeek.MONDAY, 14, 0, 15, 30);
    ClassInterval ci2 = new ClassInterval(DayOfWeek.MONDAY, 14, 30, 16, 0);
    ClassInterval ci3 = new ClassInterval(DayOfWeek.MONDAY, 14, 30, 13, 30);

    @BeforeEach
    void setUp() throws UnknownEntityException {

        Collection<Course> empty = Collections.emptyList();
        Mockito.when(courseRepository.findById(c.getID())).thenReturn(Optional.of(c));
        Mockito.when(courseRepository.findById(c2.getID())).thenReturn(Optional.empty());
        Mockito.when(courseRepository.existsById(c.getID())).thenReturn(true);
        Mockito.when(courseRepository.existsById(c2.getID())).thenReturn(false);
        Mockito.when(courseRepository.findAll()).thenReturn(List.of(c));
        Mockito.when(teacherService.readById(t.getUsername())).thenReturn(t);
        Mockito.when(teacherService.readById(t2.getUsername())).thenThrow(UnknownEntityException.class);
        Mockito.when(teacherService.readById(t3.getUsername())).thenReturn(t3);
        Mockito.when(courseRepository.findAll()).thenReturn(List.of(c));
        Mockito.when(courseRepository.findByCreditsLessThanEqual(3)).thenReturn(empty);
        Mockito.when(courseRepository.findByCreditsLessThanEqual(6)).thenReturn(List.of(c));
        Mockito.when(courseRepository.findByCapacityGreaterThanEqual(30)).thenReturn(empty);
        Mockito.when(courseRepository.findByCapacityGreaterThanEqual(40)).thenReturn(List.of(c));
        Mockito.when(courseRepository.findByCreditsAndCapacity(3, 1)).thenReturn(empty);
        Mockito.when(courseRepository.findByCreditsAndCapacity(6, 5)).thenReturn(List.of(c));

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
    void updateExists() throws UnknownEntityException {

        Course c_changed = new Course("BIE-TJV", "Java Technology", 7, 2021, 30, LocalDate.now().plusDays(1));
        c_changed.getStudents().add(st);

        this.courseService.update(c_changed);
        Mockito.verify(courseRepository, Mockito.atLeast(1)).save(c_changed);

        c_changed.setCapacity(0);
        assertThrows(InvalidParameterException.class, () -> this.courseService.update(c_changed));
        Mockito.verify(courseRepository, Mockito.atMost(1)).save(c_changed);

    }

    @Test
    void updateDoesNotExist() {

        Course c2_changed = new Course("BIE-AAG", "Only Automata but no Grammars", 8, 2020, 24, LocalDate.now().plusDays(6));

        assertThrows(UnknownEntityException.class, () -> this.courseService.update(c2_changed));
        Mockito.verify(courseRepository, Mockito.never()).save(c2_changed);

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
    void addScheduleError() {

        assertThrows(UnknownEntityException.class, () -> this.courseService.addSchedule(null, ci));
        assertThrows(UnknownEntityException.class, () -> this.courseService.addSchedule(c.getID(), null));
        assertThrows(UnknownEntityException.class, () -> this.courseService.addSchedule(c2.getID(), ci));
        assertThrows(EntityStateException.class, () -> this.courseService.addSchedule(c.getID(), ci3));

    }

    @Test
    void addScheduleOverlap() {

        c.setSchedule(List.of(ci));
        assertThrows(EntityStateException.class, () -> this.courseService.addSchedule(c.getID(), ci2));
        Mockito.verify(classIntervalRepository, Mockito.never()).save(ci2);
        Mockito.verify(courseRepository, Mockito.never()).save(c);

    }

    @Test
    void addScheduleWorking() throws Exception {

        this.courseService.addSchedule(c.getID(), ci);
        Mockito.verify(classIntervalRepository, Mockito.atLeast(1)).save(ci);
        Mockito.verify(courseRepository, Mockito.atLeast(1)).save(c);

    }

    @Test
    void removeScheduleError() {

        assertThrows(UnknownEntityException.class, () -> this.courseService.removeSchedule(null,
                ci.getDay().toString(), ci.getStart().toString(), ci.getFinish().toString()));
        assertThrows(IllegalArgumentException.class, () -> this.courseService.removeSchedule(c.getID(),
                null, null, null));
        assertThrows(UnknownEntityException.class, () -> this.courseService.removeSchedule(c2.getID(),
                ci.getDay().toString(), ci.getStart().toString(), ci.getFinish().toString()));
        assertThrows(UnknownEntityException.class, () -> this.courseService.removeSchedule(c.getID(),
                ci.getDay().toString(), ci.getStart().toString(), ci.getFinish().toString()));

    }

    @Test
    void removeScheduleWorking() throws Exception {

        c.setSchedule(List.of(ci));
        this.courseService.removeSchedule(c.getID(), ci.getDay().toString(), ci.getStart().toString(), ci.getFinish().toString());
        Mockito.verify(classIntervalRepository, Mockito.atLeast(1)).delete(ci);

    }

    @Test
    void addTeacherError() {

        assertThrows(UnknownEntityException.class, () -> this.courseService.addTeacher(null, t.getUsername()));
        assertThrows(UnknownEntityException.class, () -> this.courseService.addTeacher(c.getID(), null));
        assertThrows(EntityStateException.class, () -> this.courseService.addTeacher(c.getID(), t.getUsername()));
        assertThrows(UnknownEntityException.class, () -> this.courseService.addTeacher(c.getID(), t2.getUsername()));
        assertThrows(UnknownEntityException.class, () -> this.courseService.addTeacher(c2.getID(), t.getUsername()));
        Mockito.verify(courseRepository, Mockito.never()).save(c);

    }

    @Test
    void addTeacherWorking() throws Exception {

        this.courseService.addTeacher(c.getID(), t3.getUsername());
        Mockito.verify(courseRepository, Mockito.atLeast(1)).save(c);
        assertEquals(2, c.getTeachers().size());

    }

    @Test
    void removeTeacherError() {

        assertThrows(UnknownEntityException.class, () -> this.courseService.removeTeacher(null, t.getUsername()));
        assertThrows(UnknownEntityException.class, () -> this.courseService.removeTeacher(c.getID(), null));
        assertThrows(UnknownEntityException.class, () -> this.courseService.removeTeacher(c.getID(), t2.getUsername()));
        assertThrows(UnknownEntityException.class, () -> this.courseService.removeTeacher(c2.getID(), t.getUsername()));
        assertThrows(EntityStateException.class, () -> this.courseService.removeTeacher(c.getID(), t3.getUsername()));
        Mockito.verify(courseRepository, Mockito.never()).save(c);

    }

    @Test
    void removeTeacherWorking() throws Exception {

        this.courseService.removeTeacher(c.getID(), t.getUsername());
        Mockito.verify(courseRepository, Mockito.atLeast(1)).save(c);
        assertEquals(0, c.getTeachers().size());

    }

    @Test
    void readAllFilteredError() {

        assertThrows(InvalidParameterException.class, () -> this.courseService.readAllFiltered("-2", "0"));
        assertThrows(InvalidParameterException.class, () -> this.courseService.readAllFiltered("1", "-1"));
        assertThrows(InvalidParameterException.class, () -> this.courseService.readAllFiltered("random", "0"));
        assertThrows(InvalidParameterException.class, () -> this.courseService.readAllFiltered("1", "random"));

    }

    @Test
    void readAllFilteredWorking() {

        Collection<Course> res;

        res = this.courseService.readAllFiltered("3", "0");
        assertTrue(res.isEmpty());
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findByCreditsLessThanEqual(3);

        res = this.courseService.readAllFiltered("6", "0");
        assertTrue(res.contains(c));
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findByCreditsLessThanEqual(6);

        res = this.courseService.readAllFiltered("-1", "30");
        assertTrue(res.isEmpty());
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findByCapacityGreaterThanEqual(30);

        res = this.courseService.readAllFiltered("-1", "40");
        assertTrue(res.contains(c));
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findByCapacityGreaterThanEqual(40);

        res = this.courseService.readAllFiltered("3", "1");
        assertTrue(res.isEmpty());
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findByCreditsAndCapacity(3, 1);

        res = this.courseService.readAllFiltered("6", "5");
        assertTrue(res.contains(c));
        Mockito.verify(courseRepository, Mockito.atLeast(1)).findByCreditsAndCapacity(6, 5);

    }

}