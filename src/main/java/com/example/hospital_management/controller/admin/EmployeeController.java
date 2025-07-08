package com.example.hospital_management.controller.admin;

import com.example.hospital_management.dto.EmployeeFormDTO;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IEmployeeRoleRService employeeRoleService;

    @GetMapping()
    public String showListEmployees(Model model) {
        List<Employee> employees = employeeService.findAllEmployees();
        model.addAttribute("employees", employees);
        return "admin/employee/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        EmployeeFormDTO employeeForm = new EmployeeFormDTO();
        model.addAttribute("employeeForm", employeeForm);

        List<Department> departments = departmentService.findAllDepartmentsForDropdown();
        List<Position> positions = positionService.findAllPositions();
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("departments", departments);
        model.addAttribute("positions", positions);
        model.addAttribute("roles", roles);
        return "admin/employee/create-form";
    }

    @PostMapping("/create")
    public String createEmployee(@ModelAttribute("employeeForm") EmployeeFormDTO employeeForm) {
        Employee employee = new Employee();
        employee.setName(employeeForm.getName());
        employee.setPhone(employeeForm.getPhone());
        employee.setEmail(employeeForm.getEmail());
        employee.setPassword(employeeForm.getPassword());
        employee.setGender(employeeForm.getGender());
        employee.setAddress(employeeForm.getAddress());
        employee.setStartingDate(employeeForm.getStartingDate());
        employee.setStatus(employeeForm.getStatus());
        employee.setAvatar(employeeForm.getAvatar());

        // set department
        if (employeeForm.getDepartmentId() != null) {
            Department department = departmentService.findDepartmentById(employeeForm.getDepartmentId());
            employee.setDepartment(department);
        }
        // set position
        if (employeeForm.getPositionId() != null) {
            Position position = positionService.findPositionById(employeeForm.getPositionId());
            employee.setPosition(position);
        }
        // set role
        employeeService.saveEmployeeWithRoles(employee, employeeForm.getRoleIds());

        return "redirect:/admin/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            return "redirect:/admin/employees";
        }

        EmployeeFormDTO employeeForm = new EmployeeFormDTO();
        employeeForm.setId(employee.getId());
        employeeForm.setName(employee.getName());
        employeeForm.setPhone(employee.getPhone());
        employeeForm.setEmail(employee.getEmail());
        employeeForm.setPassword(employee.getPassword());
        employeeForm.setGender(employee.getGender());
        employeeForm.setAddress(employee.getAddress());
        employeeForm.setStartingDate(employee.getStartingDate());
        employeeForm.setStatus(employee.getStatus());
        employeeForm.setAvatar(employee.getAvatar());

        if (employee.getDepartment() != null) {
            employeeForm.setDepartmentId(employee.getDepartment().getId());
        }
        if (employee.getPosition() != null) {
            employeeForm.setPositionId(employee.getPosition().getId());
        }

        List<EmployeeRole> employeeRoles = employeeRoleService.findByEmployeeId(id);
        List<Long> currentRoleIds = employeeRoles.stream()
                .map(er -> er.getRole().getId())
                .collect(Collectors.toList());
        employeeForm.setRoleIds(currentRoleIds);

        model.addAttribute("employeeForm", employeeForm);

        model.addAttribute("departments", departmentService.findAllDepartmentsForDropdown());
        model.addAttribute("positions", positionService.findAllPositions());
        model.addAttribute("roles", roleService.findAllRoles());
        return "admin/employee/edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id, @ModelAttribute("employeeForm") EmployeeFormDTO employeeForm) {
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            return "redirect:/admin/employees";
        }

        // Update employee info
        employee.setName(employeeForm.getName());
        employee.setPhone(employeeForm.getPhone());
        employee.setEmail(employeeForm.getEmail());
        employee.setPassword(employeeForm.getPassword());
        employee.setGender(employeeForm.getGender());
        employee.setAddress(employeeForm.getAddress());
        employee.setStartingDate(employeeForm.getStartingDate());
        employee.setStatus(employeeForm.getStatus());
        employee.setAvatar(employeeForm.getAvatar());

        // Update Department
        if (employeeForm.getDepartmentId() != null) {
            Department department = departmentService.findDepartmentById(employeeForm.getDepartmentId());
            employee.setDepartment(department);
        }

        // Update Position
        if (employeeForm.getPositionId() != null) {
            Position position = positionService.findPositionById(employeeForm.getPositionId());
            employee.setPosition(position);
        }

        // Save employee
        employeeService.saveEmployee(employee);

        // Update roles
        employeeService.updateEmployeeRoles(id, employeeForm.getRoleIds());

        return "redirect:/admin/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return "redirect:/admin/employees";
    }
}
