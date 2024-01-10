/*
 *
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * import java.util.List;
 * 
 */

// Write your code here
package com.example.university.service;

import com.example.university.model.*;
import com.example.university.repository.CourseJpaRepository;
import com.example.university.repository.CourseRepository;
import com.example.university.repository.StudentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseJpaService implements CourseRepository {
    @Autowired
    private CourseJpaRepository courseJpaRepository;
    @Autowired
    private ProfessorJpaService professorJpaService;
    @Autowired
    private StudentJpaRepository studentJpaRepository;

    @Override
    public List<Course> getCourses() {
        return courseJpaRepository.findAll();
    }

    @Override
    public Course getCourseById(int courseId) {
        try {
            return courseJpaRepository.findById(courseId).get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "courseId" + courseId + " not found");
        }
    }
    @Override 
    public Course addCourse(Course course) {
        int professorId = course.getProfessor().getProfessorId();
        Professor professor = professorJpaService.getProfessorById(professorId);
        course.setProfessor(professor);

        List<Integer> studentIds = new ArrayList<>();
        for(Student student : course.getStudents()) {
            studentIds.add(student.getStudentId());
        }

        List<Student> students = studentJpaRepository.findAllById(studentIds);
        course.setStudents(students);

        return courseJpaRepository.save(course);
    }
    @Override 
    public Course updateCourse(int courseId, Course course) {
        try {
            Course newCourse = courseJpaRepository.findById(courseId).get();
            if(course.getCourseName() != null) {
                newCourse.setCourseName(course.getCourseName());
            }
            if(course.getCredits() != 0) {
                newCourse.setCredits(course.getCredits());
            }
            if(course.getProfessor() != null) {
                int professorId = course.getProfessor().getProfessorId();
                Professor professor = professorJpaService.getProfessorById(professorId);
                newCourse.setProfessor(professor);
            }
            if(course.getStudents() != null) {
                List<Integer> studentIds = new ArrayList<>();
                for(Student student : course.getStudents()) {
                    studentIds.add(student.getStudentId());
                }
                List<Student> students = studentJpaRepository.findAllById(studentIds);
                newCourse.setStudents(students);
            }
            return courseJpaRepository.save(newCourse);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "courseId " + courseId + " not found");
        }
    }
    @Override 
    public void deleteCourse(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            List<Student> students = course.getStudents();
            for(Student student : students) {
                student.getCourses().remove(course);
            }
            studentJpaRepository.saveAll(students);
            courseJpaRepository.deleteById(courseId);
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "courseId " + courseId + " not found");
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
    @Override 
    public Professor getCourseProfessor(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course.getProfessor();
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "courseId " + courseId + " not found");
        }
    }
    @Override 
    public List<Student> getCourseStudents(int courseId) {
        try {
            Course course = courseJpaRepository.findById(courseId).get();
            return course.getStudents();
        }catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "courseId " + courseId + " not found");
        }
    }
}