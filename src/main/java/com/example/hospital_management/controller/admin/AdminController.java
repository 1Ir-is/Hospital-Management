package com.example.hospital_management.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @GetMapping()
    public String showAdminDashboard(Model model) {
        model.addAttribute("activeMenu", "dashboard");
        return "admin/index";
    }
}
