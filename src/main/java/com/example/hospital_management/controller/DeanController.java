package com.example.hospital_management.controller;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DeanController {
    @Autowired
    private IEmployeeService employeeService;

    @GetMapping("/employees")
    public String viewEmployeeByDepartment(@RequestParam("departmentId") Long departmentId, Model model){
        List<Employee> employeeList =  employeeService.findByDepartment(departmentId);
        model.addAttribute("employeeList", employeeList);
        return "dean/employeeByDepartment";
    }
}
