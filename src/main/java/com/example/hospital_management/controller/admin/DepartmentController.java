package com.example.hospital_management.controller.admin;

import com.example.hospital_management.entity.Department;
import com.example.hospital_management.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @GetMapping()
    public String showListDepartments(
            Model model,
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Department> departmentPage = departmentService.findAllDepartment(pageable);

        model.addAttribute("departmentPage", departmentPage);
        model.addAttribute("departments", departmentPage.getContent());
        model.addAttribute("activeMenu", "departments");
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", departmentPage.getTotalPages());

        return "admin/department/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Department department = new Department();
        model.addAttribute("department", department);
        model.addAttribute("activeMenu", "departments-create");
        return "admin/department/create-form";
    }

    @PostMapping("/create")
    public String createDepartment(@ModelAttribute("department") Department department) {
        departmentService.saveDepartment(department);
        return "redirect:/admin/departments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.findDepartmentById(id);
        if (department == null) {
            return "redirect:/admin/departments";
        }
        model.addAttribute("activeMenu", "departments-create");
        model.addAttribute("department", department);
        return "admin/department/edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editDepartment(@PathVariable Long id, @ModelAttribute("department") Department department) {
        department.setId(id);
        departmentService.saveDepartment(department);
        return "redirect:/admin/departments";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartmentById(id);
        return "redirect:/admin/departments#deleted";
    }
}
