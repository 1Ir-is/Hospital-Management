package com.example.hospital_management.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Collection;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication, HttpServletResponse response) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
            // Lấy role đầu tiên
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                if (role.equals("ROLE_ADMIN")) {
                    response.sendRedirect("/admin");
                    return null;
                } else if (role.equals("ROLE_DEPARTMENT_HEAD")) {
                    response.sendRedirect("/department-head");
                    return null;
                } else if (role.equals("ROLE_DOCTOR")) {
                    response.sendRedirect("/doctor");
                    return null;
                } else if (role.equals("ROLE_NURSE")) {
                    response.sendRedirect("/nurse");
                    return null;
                } else if (role.equals("ROLE_RECEPTIONIST")) {
                    response.sendRedirect("/receptionist");
                    return null;
                } else if (role.equals("ROLE_LAB_TECHNICIAN")) {
                    response.sendRedirect("/lab-technician");
                    return null;
                } else if (role.equals("ROLE_CASHIER")) {
                    response.sendRedirect("/cashier");
                    return null;
                } else if (role.equals("ROLE_PHARMACY_STAFF")) {
                    response.sendRedirect("/pharmacy");
                    return null;
                } else if (role.equals("ROLE_PATIENT")) {
                    response.sendRedirect("/patient");
                    return null;
                }
            }
        }
        // Nếu chưa đăng nhập, hiển thị trang chủ như bình thường
        return "index";
    }
}