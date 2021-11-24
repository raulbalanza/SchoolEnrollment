package me.raulbalanza.tjv.school_enrollment.dao;

import me.raulbalanza.tjv.school_enrollment.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> { }
