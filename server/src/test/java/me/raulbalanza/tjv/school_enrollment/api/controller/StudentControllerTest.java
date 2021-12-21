package me.raulbalanza.tjv.school_enrollment.api.controller;

import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.StudentService;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import me.raulbalanza.tjv.school_enrollment.domain.Course;
import me.raulbalanza.tjv.school_enrollment.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    Course c = new Course("BIE-TJV", "Java Technology", 6, 2021, 30, LocalDate.now().plusDays(1));
    Course c2 = new Course("BIE-AAG", "Automata and Grammars", 8, 2020, 24, LocalDate.now().plusDays(6));
    Student st = new Student("balanrau", "12345678A", "12345", "balanrau@fit.cvut.cz",
            "Raul", "Balanza Garcia", LocalDate.of(2000, 1, 30), 3);
    Student st2 = new Student("teststudent", "12345678C", "12345", "testing@fit.cvut.cz",
            "Test", "Student", LocalDate.of(2001, 6, 25), 2);

    @BeforeEach
    void setUp() throws EntityStateException, UnknownEntityException {

        Mockito.when(studentService.readAll()).thenReturn(List.of(st));
        Mockito.when(studentService.readById(st.getUsername())).thenReturn(st);
        Mockito.doThrow(new UnknownEntityException("-")).when(studentService).deleteById(st2.getUsername());
        Mockito.when(studentService.readById(st2.getUsername())).thenThrow(new UnknownEntityException("-"));
        Mockito.doThrow(new EntityStateException("-")).when(studentService).create(st);

        st.getEnrolledCourses().add(c);
        c.getStudents().add(st);

    }

    @Test
    void getAll() throws Exception {

        mockMvc.perform(get("/students").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("balanrau"));

    }

    @Test
    void createStudentNew() throws Exception {

        String jsonStudent = "{\n" +
                "    \"username\": \"teststudent\",\n" +
                "    \"ID\": \"12345678D\",\n" +
                "    \"email\": \"testing.student@fit.cvut.cz\",\n" +
                "    \"name\": \"Test\",\n" +
                "    \"surnames\": \"Student\",\n" +
                "    \"password\": \"123456\",\n" +
                "    \"currentYear\": \"3\"\n" +
                "}";

        mockMvc.perform(post("/students").contentType("application/json").content(jsonStudent)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(st2.getUsername()));
        Mockito.verify(studentService, Mockito.atLeast(1)).create(st2);

    }

    @Test
    void createStudentExisting() throws Exception {

        String jsonStudent = "{\n" +
                "    \"username\": \"balanrau\",\n" +
                "    \"ID\": \"12345678A\",\n" +
                "    \"email\": \"balanrau@fit.cvut.cz\",\n" +
                "    \"name\": \"Raul\",\n" +
                "    \"surnames\": \"Balanza\",\n" +
                "    \"birthDate\": \"30.01.2000\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"currentYear\": 3\n" +
                "}";

        mockMvc.perform(post("/students").contentType("application/json").content(jsonStudent)
                        .accept("application/json"))
                .andExpect(status().is(409));
        Mockito.verify(studentService, Mockito.atLeast(1)).create(st);

    }

    @Test
    void getOneExisting() throws Exception {

        mockMvc.perform(get("/students/balanrau").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("balanrau"));
        Mockito.verify(studentService, Mockito.atLeast(1)).readById(st.getUsername());

    }

    @Test
    void getOneNonExisting() throws Exception {

        mockMvc.perform(get("/students/teststudent").accept("application/json"))
                .andExpect(status().is(404));
        Mockito.verify(studentService, Mockito.atLeast(1)).readById(st2.getUsername());

    }

    @Test
    void updateStudent() throws Exception {

        String jsonStudent = "{\n" +
                "    \"username\": \"balanrau\",\n" +
                "    \"ID\": \"12345678A\",\n" +
                "    \"email\": \"raubagar@inf.upv.es\",\n" +
                "    \"name\": \"Raul\",\n" +
                "    \"surnames\": \"Balanza\",\n" +
                "    \"birthDate\": \"30.01.2000\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"currentYear\": 3\n" +
                "}";
        st.setEmail("raubagar@inf.upv.es");

        mockMvc.perform(put("/students/balanrau").contentType("application/json").content(jsonStudent)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("balanrau"))
                .andExpect(jsonPath("$.email").value("raubagar@inf.upv.es"));
        Mockito.verify(studentService, Mockito.atLeast(1)).update(st);

    }

    @Test
    void updateStudentIdMismatch() throws Exception {

        String jsonStudent = "{\n" +
                "    \"username\": \"balanrau\",\n" +
                "    \"ID\": \"12345678A\",\n" +
                "    \"email\": \"raubagar@inf.upv.es\",\n" +
                "    \"name\": \"Raul\",\n" +
                "    \"surnames\": \"Balanza\",\n" +
                "    \"birthDate\": \"30.01.2000\",\n" +
                "    \"password\": \"1234\",\n" +
                "    \"currentYear\": 3\n" +
                "}";
        st.setEmail("raubagar@inf.upv.es");

        mockMvc.perform(put("/students/teststudent").contentType("application/json").content(jsonStudent)
                        .accept("application/json"))
                .andExpect(status().is(400));
        Mockito.verify(studentService, Mockito.never()).update(st);
        Mockito.verify(studentService, Mockito.never()).update(st2);

    }

    @Test
    void deleteStudentExisting() throws Exception {

        mockMvc.perform(delete("/students/balanrau").accept("application/json"))
                .andExpect(status().is(204));
        Mockito.verify(studentService, Mockito.atLeast(1)).deleteById(st.getUsername());

    }

    @Test
    void deleteStudentNonExisting() throws Exception {

        mockMvc.perform(delete("/students/teststudent").accept("application/json"))
                .andExpect(status().is(404));
        Mockito.verify(studentService, Mockito.atLeast(1)).deleteById(st2.getUsername());

    }

    @Test
    void getEnrolledCourses() throws Exception {

        mockMvc.perform(get("/students/balanrau/courses").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ID").value("BIE-TJV"));
        Mockito.verify(studentService, Mockito.atLeast(1)).readById(st.getUsername());

    }

    @Test
    void enrollInCourse() throws Exception {

        mockMvc.perform(post("/students/balanrau/courses/BIE-TJV").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ID").value("BIE-TJV"));
        Mockito.verify(studentService, Mockito.atLeast(1)).enrollCourse(st.getUsername(), c.getID());

    }

    @Test
    void removeFromCourse() throws Exception {

        st.getEnrolledCourses().remove(c);

        mockMvc.perform(delete("/students/balanrau/courses/BIE-TJV").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        Mockito.verify(studentService, Mockito.atLeast(1)).abandonCourse(st.getUsername(), c.getID());

    }
}