package me.raulbalanza.tjv.school_enrollment.api.controller;

import me.raulbalanza.tjv.school_enrollment.business.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;

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

    @Test
    void getAll() throws Exception {

        Mockito.when(courseService.readAll()).thenReturn(Collections.EMPTY_LIST);

        mockMvc.perform(get("/courses").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

    }

    @Test
    void createCourse() {
    }

    @Test
    void getOne() {
    }

    @Test
    void updateCourse() {
    }

    @Test
    void deleteCourse() {
    }

    @Test
    void getSchedule() {
    }

    @Test
    void addSchedule() {
    }

    @Test
    void deleteSchedule() {
    }

    @Test
    void getTeachersInCourse() {
    }

    @Test
    void teachCourse() {
    }

    @Test
    void removeCourse() {
    }
}