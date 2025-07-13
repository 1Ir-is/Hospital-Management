package com.example.hospital_management.controller.employee_assignment;


import com.example.hospital_management.service.IEmployeeAssignmentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee-assignment")
public class EmployeeAssignmentController {
    private final IEmployeeAssignmentService employeeAssignmentService;
    public EmployeeAssignmentController(IEmployeeAssignmentService employeeAssignmentService) {
        this.employeeAssignmentService = employeeAssignmentService;
    }




}
