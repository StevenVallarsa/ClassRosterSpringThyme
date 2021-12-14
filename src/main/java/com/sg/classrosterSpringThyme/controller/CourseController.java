/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.sg.classrosterSpringThyme.controller;

import com.sg.classrosterSpringThyme.dao.CourseDao;
import com.sg.classrosterSpringThyme.dao.StudentDao;
import com.sg.classrosterSpringThyme.dao.TeacherDao;
import com.sg.classrosterSpringThyme.dto.Course;
import com.sg.classrosterSpringThyme.dto.Student;
import com.sg.classrosterSpringThyme.dto.Teacher;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author: Steven Vallarsa
 * email: stevenvallarsa@gmail.com
 * date:
 * purpose:
 */
@Controller
public class CourseController {

   @Autowired
   TeacherDao teacherDao;

   @Autowired
   StudentDao studentDao;

   @Autowired
   CourseDao courseDao;
   
   boolean tCourses = false;
      
    @GetMapping("courses")
    public String displayCourses(Model model) {
        List<Course> courses = courseDao.getAllCourses();
        List<Teacher> teachers = teacherDao.getAllTeachers();
        List<Student> students = studentDao.getAllStudents();
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        return "courses";
    }
    
    @PostMapping("addCourse")
    public String addCourse(Course course, HttpServletRequest request) {
        String teacherId = request.getParameter("teacherId");
        String[] studentIds = request.getParameterValues("studentId");
        
        course.setTeacher(teacherDao.getTeacherById(Integer.parseInt(teacherId)));
        
        List<Student> students = new ArrayList<>();
        if (studentIds != null) {
            return "redirect:/courses";
        }
        
        course.setStudents(students);
        courseDao.addCourse(course);
        
        return "redirect:/courses";
    }
    
    @GetMapping("courseDetail")
    public String courseDetail(Integer id, Model model) {
        Course course = courseDao.getCourseById(id);
        model.addAttribute("course", course);
        return "courseDetail";
    }
    
    @GetMapping("deleteCourse")
    public String deleteCourse(Integer id) {
        courseDao.deleteCourseById(id);
        return "redirect:/courses";
    }
    
    @GetMapping("editCourse")
    public String editCourse(Integer id, Model model) {
        Course course = courseDao.getCourseById(id);
        List<Student> students = studentDao.getAllStudents();
        List<Teacher> teachers = teacherDao.getAllTeachers();
        model.addAttribute("course", course);
        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);
        return "editCourse";
    }
    
    @PostMapping("editCourse")
    public String performEditCourse(@Valid Course course, BindingResult result, HttpServletRequest request, Model model) {
        
        // doing this behind the scenes
        // course.setName(request.getAttribute("name")); ...
        
        String teacherId = request.getParameter("teacherId");
        String[] studentIds = request.getParameterValues("studentId");
        
        course.setTeacher(teacherDao.getTeacherById(Integer.parseInt(teacherId)));
        
        List<Student> students = new ArrayList<>();
        
        // checking to see if any students were put in since we need
        // an array of students to fill the roster. Output error is no students.
        if (studentIds != null) {
            for(String studentId : studentIds) {
                students.add(studentDao.getStudentById(Integer.parseInt(studentId)));
            }
        } else {
            FieldError error = new FieldError("course", "students", "Must include one student");
            result.addError(error);
        }
        
        course.setStudents(students);
        
        if (result.hasErrors()) {
            model.addAttribute("teachers", teacherDao.getAllTeachers());
            model.addAttribute("students", studentDao.getAllStudents());
            model.addAttribute("course", course);
            return "editCourse";
        }
        
        courseDao.updateCourse(course);
        
        return "redirect:/courses";
    }
    
    @GetMapping("teacherCourse")
    public String findCourseForTeacher(Integer teacherId, Model model) {
        List<Course> teacherCourses = courseDao.getCoursesForTeacher(teacherDao.getTeacherById(teacherId));
        
        List<Course> courses = courseDao.getAllCourses();
        List<Teacher> teachers = teacherDao.getAllTeachers();
        List<Student> students = studentDao.getAllStudents();
        boolean tCourses = true;

        
        model.addAttribute("courses", courses);
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        model.addAttribute("teacherCourses", teacherCourses);
        model.addAttribute("tCourses", tCourses);
        
        return "courses";
    }
    
    
}
