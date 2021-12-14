/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sg.classrosterSpringThyme.dao;

import com.sg.classrosterSpringThyme.dto.Course;
import com.sg.classrosterSpringThyme.dto.Student;
import com.sg.classrosterSpringThyme.dto.Teacher;
import java.util.List;

/**
 *
 * @author StevePro
 */
public interface CourseDao {
    Course getCourseById(int id);
    List<Course> getAllCourses();
    Course addCourse(Course course);
    void updateCourse(Course course);
    void deleteCourseById(int id);
    
    List<Course> getCoursesForTeacher(Teacher teacher);
    List<Course> getCoursesForStudent(Student student);
}