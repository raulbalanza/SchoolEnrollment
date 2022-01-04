package me.raulbalanza.tjv.school_enrollment.api.controller;

import me.raulbalanza.tjv.school_enrollment.business.CourseService;
import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import me.raulbalanza.tjv.school_enrollment.domain.ClassInterval;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
import org.hibernate.event.internal.EntityState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    Course c = new Course("BIE-TJV", "Java Technology", 6, 2021, 30, LocalDate.now().plusDays(1));
    Course c2 = new Course("BIE-AAG", "Automata and Grammars", 8, 2020, 24, LocalDate.now().plusDays(6));
    Student st = new Student("balanrau", "12345678A", "12345", "balanrau@fit.cvut.cz",
            "Raul", "Balanza Garcia", LocalDate.of(2000, 1, 30), 3);
    Teacher t = new Teacher("guthondr", "12345678B", "123456", "ondrej.guth@fit.cvut.cz",
            "Ondrej", "Guth", LocalDate.of(1995, 4, 5), "Teacher");
    ClassInterval ci = new ClassInterval(DayOfWeek.MONDAY, 14, 0, 15, 30);

    @BeforeEach
    void setUp() throws EntityStateException, UnknownEntityException {
        Mockito.when(courseService.readAllFiltered("-1", "0")).thenReturn(List.of(c));
        Mockito.when(courseService.readById(c.getID())).thenReturn(c);
        Mockito.when(courseService.readById(c2.getID())).thenThrow(new UnknownEntityException("-"));
        Mockito.doThrow(new EntityStateException("-")).when(courseService).create(c);
        Mockito.doThrow(new UnknownEntityException("-")).when(courseService).deleteById(c2.getID());

        t.getTeachingCourses().add(c);
        c.getTeachers().add(t);
        c.setSchedule(List.of(ci));
        st.getEnrolledCourses().add(c);
        c.getStudents().add(st);
    }

    @Test
    void getAll() throws Exception {

        mockMvc.perform(get("/courses").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ID").value("BIE-TJV"));

    }

    @Test
    void createCourseNew() throws Exception {

        String jsonCourse = "{\n" +
                "    \"ID\": \"BIE-AAG\",\n" +
                "    \"name\": \"Automata and Grammars\",\n" +
                "    \"credits\": 6,\n" +
                "    \"year\": 2021,\n" +
                "    \"enrollLimit\": \"9.12.2021\",\n" +
                "    \"capacity\": 25\n" +
                "}";

        mockMvc.perform(post("/courses").contentType("application/json").content(jsonCourse)
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ID").value("BIE-AAG"));
        Mockito.verify(courseService, Mockito.atLeast(1)).create(c2);

    }

    @Test
    void createCourseExisting() throws Exception {

        String jsonCourse = "{\n" +
                "    \"ID\": \"BIE-TJV\",\n" +
                "    \"name\": \"Java Technology\",\n" +
                "    \"credits\": 6,\n" +
                "    \"year\": 2021,\n" +
                "    \"enrollLimit\": \"9.12.2021\",\n" +
                "    \"capacity\": 30\n" +
                "}";

        mockMvc.perform(post("/courses").contentType("application/json").content(jsonCourse)
                .accept("application/json"))
                .andExpect(status().is(409));
        Mockito.verify(courseService, Mockito.atLeast(1)).create(c);

    }

    @Test
    void getOneExisting() throws Exception {

        mockMvc.perform(get("/courses/BIE-TJV").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ID").value("BIE-TJV"));
        Mockito.verify(courseService, Mockito.atLeast(1)).readById(c.getID());

    }

    @Test
    void getOneNonExisting() throws Exception {

        mockMvc.perform(get("/courses/BIE-AAG").accept("application/json"))
                .andExpect(status().is(404));
        Mockito.verify(courseService, Mockito.atLeast(1)).readById(c2.getID());

    }

    @Test
    void updateCourseExisting() throws Exception {

        String jsonCourse = "{\n" +
                "    \"ID\": \"BIE-TJV\",\n" +
                "    \"name\": \"Java Technology\",\n" +
                "    \"credits\": 8,\n" +
                "    \"year\": 2021,\n" +
                "    \"enrollLimit\": \"9.12.2021\",\n" +
                "    \"capacity\": 30\n" +
                "}";
        c.setCredits(8);

        mockMvc.perform(put("/courses/BIE-TJV").contentType("application/json").content(jsonCourse)
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ID").value("BIE-TJV"))
                .andExpect(jsonPath("$.credits").value(8));
        Mockito.verify(courseService, Mockito.atLeast(1)).update(c);

    }

    @Test
    void updateCourseNonExisting() throws Exception {

        String jsonCourse = "{\n" +
                "    \"ID\": \"BIE-AAG\",\n" +
                "    \"name\": \"Automata and Grammars\",\n" +
                "    \"credits\": 3,\n" +
                "    \"year\": 2021,\n" +
                "    \"enrollLimit\": \"9.12.2021\",\n" +
                "    \"capacity\": 25\n" +
                "}";

        mockMvc.perform(put("/courses/BIE-AAG").contentType("application/json").content(jsonCourse)
                .accept("application/json"))
                .andExpect(status().is(404));
        Mockito.verify(courseService, Mockito.atLeast(1)).update(c2);

    }

    @Test
    void updateCourseIdMismatch() throws Exception {

        String jsonCourse = "{\n" +
                "    \"ID\": \"BIE-AAG\",\n" +
                "    \"name\": \"Automata and Grammars\",\n" +
                "    \"credits\": 3,\n" +
                "    \"year\": 2021,\n" +
                "    \"enrollLimit\": \"9.12.2021\",\n" +
                "    \"capacity\": 25\n" +
                "}";

        mockMvc.perform(put("/courses/BIE-TJV").contentType("application/json").content(jsonCourse)
                .accept("application/json"))
                .andExpect(status().is(400));
        Mockito.verify(courseService, Mockito.never()).update(c);
        Mockito.verify(courseService, Mockito.never()).update(c2);

    }

    @Test
    void deleteCourseExisting() throws Exception {

        mockMvc.perform(delete("/courses/BIE-TJV").accept("application/json"))
                .andExpect(status().is(204));
        Mockito.verify(courseService, Mockito.atLeast(1)).deleteById(c.getID());

    }

    @Test
    void deleteCourseNonExisting() throws Exception {

        mockMvc.perform(delete("/courses/BIE-AAG").accept("application/json"))
                .andExpect(status().is(404));
        Mockito.verify(courseService, Mockito.atLeast(1)).deleteById(c2.getID());

    }

    @Test
    void getSchedule() throws Exception {

        mockMvc.perform(get("/courses/BIE-TJV/schedule").accept("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].day").value("MONDAY"));
        Mockito.verify(courseService, Mockito.atLeast(1)).readById(c.getID());

    }

    @Test
    void addSchedule() throws Exception {

        String jsonInterval = "{\n" +
                "    \"day\": \"MONDAY\",\n" +
                "    \"start\": \"14:00\",\n" +
                "    \"finish\": \"15:30\"\n" +
                "}";

        mockMvc.perform(post("/courses/BIE-TJV/schedule").contentType("application/json").content(jsonInterval)
                .accept("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.day").value("MONDAY"));
        Mockito.verify(courseService, Mockito.atLeast(1)).addSchedule(c.getID(), ci);

    }

    @Test
    void deleteSchedule() throws Exception {

        c.setSchedule(Collections.emptyList());

        mockMvc.perform(delete("/courses/BIE-TJV/schedule?day=" + ci.getDay() + "&start=" + ci.getStart() + "&finish=" + ci.getFinish())
                        .accept("application/json"))
                .andExpect(status().is(204));
        Mockito.verify(courseService, Mockito.atLeast(1)).removeSchedule(c.getID(),
                ci.getDay().toString(), ci.getStart().toString(), ci.getFinish().toString());

    }

    @Test
    void getTeachersInCourse() throws Exception {

        mockMvc.perform(get("/courses/BIE-TJV/teachers").accept("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("guthondr"));
        Mockito.verify(courseService, Mockito.atLeast(1)).readById(c.getID());

    }

    @Test
    void teachCourse() throws Exception {

        mockMvc.perform(post("/courses/BIE-TJV/teachers/" + t.getUsername()).accept("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(1)));
        Mockito.verify(courseService, Mockito.atLeast(1)).addTeacher(c.getID(), t.getUsername());

    }

    @Test
    void removeTeacherCourse() throws Exception {

        c.getTeachers().remove(t);

        mockMvc.perform(delete("/courses/BIE-TJV/teachers/" + t.getUsername()).accept("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
        Mockito.verify(courseService, Mockito.atLeast(1)).removeTeacher(c.getID(), t.getUsername());

    }
}