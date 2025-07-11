package com.example.hospital_management.controller;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserInfoController {

    @Autowired
    private IEmployeeRepository employeeRepository;

    @ModelAttribute
    public void addUserInfoToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {

            String email = authentication.getName();
            Employee employee = employeeRepository.findByEmail(email);

            if (employee != null) {
                model.addAttribute("currentUser", employee);
                model.addAttribute("currentUserName", employee.getName());
                model.addAttribute("currentUserEmail", employee.getEmail());
                model.addAttribute("currentUserAvatar", getAvatarInitial(employee.getName()));
                // ✅ Thêm URL avatar thật
                model.addAttribute("currentUserAvatarUrl", employee.getAvatar());
                model.addAttribute("hasAvatar", employee.getAvatar() != null && !employee.getAvatar().trim().isEmpty());
            }
        }
    }

    private String getAvatarInitial(String name) {
        if (name != null && !name.trim().isEmpty()) {
            // Lấy chữ cái đầu của tên
            return name.trim().substring(0, 1).toUpperCase();
        }
        return "U"; // Default
    }
}