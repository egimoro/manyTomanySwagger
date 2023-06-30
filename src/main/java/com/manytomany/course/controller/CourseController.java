package com.manytomany.course.controller;

import com.manytomany.course.entities.Course;
import com.manytomany.course.repositories.CourseRepository;
import com.manytomany.course.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @GetMapping("/instructors/{instructorId}/courses")
    public List<Course> getCoursesByInstructor(@PathVariable(value = "instructorId") Long instructorId){
        return courseRepository.findByInstructorId(instructorId);

    }

    @PostMapping("/instructors/{instructorId}/courses")
    public Course createCourse(@PathVariable(value = "instructorId") Long instructorId,
                               @RequestBody Course course) throws ResourceNotFoundException{
        return instructorRepository.findById(instructorId).map(instructor -> {
            course.setInstructor(instructor);
            return courseRepository.save(course);
        }).orElseThrow(()-> new ResourceNotFoundException("Instructor not found"));
    }

    @PutMapping("/instructors/{instructorId}/courses/{courseId}")
    public Course updateCourse(@PathVariable(value = "instructorId")Long instructorId,
                               @PathVariable(value = "courseId") Long courseId,
                               @RequestBody Course courseRequest)
        throws ResourceNotFoundException{
        if (!instructorRepository.existsById(instructorId)){
            throw new ResourceNotFoundException("InstructorId not found");
        }
        return courseRepository.findById(courseId).map(course -> {
            course.setTitle(courseRequest.getTitle());
            return courseRepository.save(course);
        }).orElseThrow(()-> new ResourceNotFoundException("Course id not found"));
    }

    @DeleteMapping("/instructors/{instructorId}/courses/{courseId}")
    public ResponseEntity<?>deleteCourse(@PathVariable(value = "instructorId") Long instructorId,
                                         @PathVariable(value = "courseId") Long courseId)
        throws ResourceNotFoundException{
        return courseRepository.findByIdAndInstructorId(courseId, instructorId).map(course -> {
            courseRepository.delete(course);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException(
           "Course not found with id " + courseId + " and instructorId " + instructorId
        ));
    }
}
