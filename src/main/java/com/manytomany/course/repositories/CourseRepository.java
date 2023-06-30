package com.manytomany.course.repositories;

import com.manytomany.course.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructorId(Long instructorId);
    Optional<Course> findByIdAndInstructorId(Long id, Long instructorId);
}
