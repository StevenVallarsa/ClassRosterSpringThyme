/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.sg.classrosterSpringThyme.controller;

import com.sg.classrosterSpringThyme.dao.CourseDao;
import com.sg.classrosterSpringThyme.dao.StudentDao;
import com.sg.classrosterSpringThyme.dao.TeacherDao;
import com.sg.classrosterSpringThyme.dto.Student;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
public class StudentController {

    @Autowired
    TeacherDao teacherDao;
    
    @Autowired
    StudentDao studentDao;
    
    @Autowired
    CourseDao courseDao;
    
    @GetMapping("students")
    public String displayStudents(Model model) {
        List<Student> students = studentDao.getAllStudents();
        model.addAttribute("students", students);
        return "students";
    }
    
    @PostMapping("addStudent")
    public String addStudent(String firstName, String lastName) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        studentDao.addStudent(student);
        
        return "redirect:/students";        
    }
    
    @GetMapping("deleteStudent")
    public String deleteStudent(Integer id) {
        studentDao.deleteStudentById(id);
        return "redirect:/students";
    }
    
@PostMapping("editStudent")
public String performEditStudent(@Valid Student student, BindingResult result) {
    if(result.hasErrors()) {
        return "editStudent";
    }
    studentDao.updateStudent(student);
    return "redirect:/students";
}
    
    @GetMapping("editStudent")
    public String editStudent(int id, Model model) {
        Student student = studentDao.getStudentById(id);
        model.addAttribute("student", student);
        return "editStudent";
    }

}
