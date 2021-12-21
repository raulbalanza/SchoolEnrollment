package me.raulbalanza.tjv.school_enrollment.api.controller;

import me.raulbalanza.tjv.school_enrollment.business.EntityStateException;
import me.raulbalanza.tjv.school_enrollment.business.TeacherService;
import me.raulbalanza.tjv.school_enrollment.business.UnknownEntityException;
import me.raulbalanza.tjv.school_enrollment.domain.Teacher;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    Teacher t = new Teacher("guthondr", "12345678B", "123456", "ondrej.guth@fit.cvut.cz",
            "Ondrej", "Guth", LocalDate.of(1995, 4, 5), "Teacher");
    Teacher t2 = new Teacher("testteacher", "12345678D", "123456", "testing.teacher@fit.cvut.cz",
            "Test", "Teacher", LocalDate.of(1993, 3, 15), "Supervisor");

    @BeforeEach
    void setUp() throws EntityStateException, UnknownEntityException {

        Mockito.when(teacherService.readAll()).thenReturn(List.of(t));
        Mockito.when(teacherService.readById(t.getUsername())).thenReturn(t);
        Mockito.doThrow(new UnknownEntityException("-")).when(teacherService).deleteById(t2.getUsername());
        Mockito.when(teacherService.readById(t2.getUsername())).thenThrow(new UnknownEntityException("-"));
        Mockito.doThrow(new EntityStateException("-")).when(teacherService).create(t);

    }

    @Test
    void getAll() throws Exception {

        mockMvc.perform(get("/teachers").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username").value("guthondr"));

    }

    @Test
    void createTeacherNew() throws Exception {

        String jsonTeacher = "{\n" +
                "    \"username\": \"testteacher\",\n" +
                "    \"ID\": \"12345678D\",\n" +
                "    \"email\": \"testing.teacher@fit.cvut.cz\",\n" +
                "    \"name\": \"Test\",\n" +
                "    \"surnames\": \"Teacher\",\n" +
                "    \"password\": \"123456\",\n" +
                "    \"rank\": \"Supervisor\"\n" +
                "}";

        mockMvc.perform(post("/teachers").contentType("application/json").content(jsonTeacher)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(t2.getUsername()));
        Mockito.verify(teacherService, Mockito.atLeast(1)).create(t2);

    }

    @Test
    void createTeacherExisting() throws Exception {

        String jsonTeacher = "{\n" +
                "    \"username\": \"guthondr\",\n" +
                "    \"ID\": \"12345678B\",\n" +
                "    \"email\": \"ondrej.guth@fit.cvut.cz\",\n" +
                "    \"name\": \"Ondrej\",\n" +
                "    \"surnames\": \"Guth\",\n" +
                "    \"password\": \"tjv1234\",\n" +
                "    \"rank\": \"Department leader\"\n" +
                "}";

        mockMvc.perform(post("/teachers").contentType("application/json").content(jsonTeacher)
                        .accept("application/json"))
                .andExpect(status().is(409));
        Mockito.verify(teacherService, Mockito.atLeast(1)).create(t);

    }

    @Test
    void getOneExisting() throws Exception {

        mockMvc.perform(get("/teachers/guthondr").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("guthondr"));
        Mockito.verify(teacherService, Mockito.atLeast(1)).readById(t.getUsername());

    }

    @Test
    void getOneNonExisting() throws Exception {

        mockMvc.perform(get("/teachers/testteacher").accept("application/json"))
                .andExpect(status().is(404));
        Mockito.verify(teacherService, Mockito.atLeast(1)).readById(t2.getUsername());

    }

    @Test
    void updateTeacher() throws Exception {

        String jsonTeacher = "{\n" +
                "    \"username\": \"guthondr\",\n" +
                "    \"ID\": \"12345678B\",\n" +
                "    \"email\": \"temp_email@fit.cvut.cz\",\n" +
                "    \"name\": \"Ondrej\",\n" +
                "    \"surnames\": \"Guth\",\n" +
                "    \"password\": \"tjv1234\",\n" +
                "    \"rank\": \"Department leader\"\n" +
                "}";
        t.setEmail("temp_email@fit.cvut.cz");

        mockMvc.perform(put("/teachers/guthondr").contentType("application/json").content(jsonTeacher)
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("guthondr"))
                .andExpect(jsonPath("$.email").value("temp_email@fit.cvut.cz"));
        Mockito.verify(teacherService, Mockito.atLeast(1)).update(t);

    }

    @Test
    void updateTeacherIdMismatch() throws Exception {

        String jsonTeacher = "{\n" +
                "    \"username\": \"guthondr\",\n" +
                "    \"ID\": \"12345678B\",\n" +
                "    \"email\": \"temp_email@fit.cvut.cz\",\n" +
                "    \"name\": \"Ondrej\",\n" +
                "    \"surnames\": \"Guth\",\n" +
                "    \"password\": \"tjv1234\",\n" +
                "    \"rank\": \"Department leader\"\n" +
                "}";
        t.setEmail("temp_email@fit.cvut.cz");

        mockMvc.perform(put("/teachers/testteacher").contentType("application/json").content(jsonTeacher)
                        .accept("application/json"))
                .andExpect(status().is(400));
        Mockito.verify(teacherService, Mockito.never()).update(t);
        Mockito.verify(teacherService, Mockito.never()).update(t2);

    }

    @Test
    void deleteTeacherExisting() throws Exception {

        mockMvc.perform(delete("/teachers/guthondr").accept("application/json"))
                .andExpect(status().is(204));
        Mockito.verify(teacherService, Mockito.atLeast(1)).deleteById(t.getUsername());

    }

    @Test
    void deleteTeacherNonExisting() throws Exception {

        mockMvc.perform(delete("/teachers/testteacher").accept("application/json"))
                .andExpect(status().is(404));
        Mockito.verify(teacherService, Mockito.atLeast(1)).deleteById(t2.getUsername());

    }
}