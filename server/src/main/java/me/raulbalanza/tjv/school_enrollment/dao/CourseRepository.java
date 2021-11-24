package me.raulbalanza.tjv.school_enrollment.dao;

import me.raulbalanza.tjv.school_enrollment.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    // Filter by maximum number of credits and minimum free slots using SQL
    @Query(nativeQuery = true, value = "SELECT * FROM course c WHERE c.credits <= :max_credits " +
            "AND (c.capacity - (SELECT COUNT(*) FROM enrolled_courses e WHERE e.course_id = c.id)) >= :min_free_capacity")
    Collection<Course> readAllFilterCreditsCapacity(int max_credits, int min_free_capacity);

    // Filter only by maximum number of credits using JPA syntax
    @Query("SELECT c FROM Course c WHERE c.credits <= :max_credits")
    Collection<Course> readAllFilterCredits(int max_credits);

    // Filter only by minimum free slots using SQL
    @Query(nativeQuery = true, value = "SELECT * FROM course c " +
            "WHERE (c.capacity - (SELECT COUNT(*) FROM enrolled_courses e WHERE e.course_id = c.id)) >= :min_free_capacity")
    Collection<Course> readAllFilterCapacity(int min_free_capacity);

}
