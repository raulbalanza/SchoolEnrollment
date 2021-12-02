package me.raulbalanza.tjv.school_enrollment.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        // Insert data before each test
    }

    @AfterEach
    void tearDown() {
        // Clean DB after each test
        courseRepository.deleteAll();
    }

    @Test
    void readAllFilterCreditsCapacity() {
    }

    @Test
    void readAllFilterCredits() {
    }

    @Test
    void readAllFilterCapacity() {
    }
}