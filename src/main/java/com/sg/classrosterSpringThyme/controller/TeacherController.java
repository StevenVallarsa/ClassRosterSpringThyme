/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.sg.classrosterSpringThyme.controller;

import com.sg.classrosterSpringThyme.dao.CourseDao;
import com.sg.classrosterSpringThyme.dao.StudentDao;
import com.sg.classrosterSpringThyme.dao.TeacherDao;
import com.sg.classrosterSpringThyme.dto.Teacher;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class TeacherController {
    
    @Autowired
    TeacherDao teacherDao;
    
    @Autowired
    StudentDao studentDao;
    
    @Autowired
    CourseDao courseDao;
    
    // VALIDATION START
    Set<ConstraintViolation<Teacher>> violations = new HashSet<>();
    // END VALIDATION
    
    @GetMapping("teachers")
    public String displayTeacher(Model model) {
        List<Teacher> teachers = teacherDao.getAllTeachers();
        model.addAttribute("teachers", teachers);
        model.addAttribute("errors", violations);
        
        return "teachers"; // uses template called "teachers" -- nothing to do with Dao.
    }
    
    @PostMapping("addTeacher")
    public String addTeacher(HttpServletRequest request) {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String specialty = request.getParameter("specialty");
        
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setSpecialty(specialty);
        
        //teacherDao.addTeacher(teacher);
        // VALIDATION START - replaces above line 
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(teacher);

        if(violations.isEmpty()) {
            teacherDao.addTeacher(teacher);
        }

        // END VALIDATION
        
        return "redirect:/teachers";
    }
    
    @GetMapping("deleteTeacher")
    public String deleteTeacher(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        teacherDao.deleteTeacherById(id);
        
        return "redirect:/teachers";
    }

    @GetMapping("editTeacher")
    public String editTeacher(HttpServletRequest request, Model model) {
        int id = Integer.parseInt(request.getParameter("id"));
        Teacher teacher = teacherDao.getTeacherById(id);
        
        model.addAttribute("teacher", teacher);
        return "editTeacher";
    }

    @PostMapping("editTeacher")
    public String performEditTeacher(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Teacher teacher = teacherDao.getTeacherById(id);
        
        teacher.setFirstName(request.getParameter("firstName"));
        teacher.setLastName(request.getParameter("lastName"));
        teacher.setSpecialty(request.getParameter("specialty"));
        
        teacherDao.updateTeacher(teacher);
        
        return "redirect:/teachers";
    }
}
