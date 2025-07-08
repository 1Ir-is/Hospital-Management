package com.example.hospital_management.controller.admin;

import com.example.hospital_management.entity.Role;
import com.example.hospital_management.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping()
    public String showListRoles(Model model) {
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("roles", roles);
        return "admin/role/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Role role = new Role();
        model.addAttribute("role", role);
        return "admin/role/create-form";
    }

    @PostMapping("/create")
    public String createRole(@ModelAttribute("role") Role role) {
        roleService.saveRole(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Role role = roleService.findRoleById(id);
        if (role == null) {
            return "redirect:/admin/roles";
        }
        model.addAttribute("role", role);
        return "admin/role/edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editRole(@PathVariable Long id, @ModelAttribute("role") Role role) {
        role.setId(id);
        roleService.saveRole(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleService.deleteRoleById(id);
        return "redirect:/admin/roles";
    }
}
