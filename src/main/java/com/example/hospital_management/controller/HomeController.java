package com.example.hospital_management.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collection;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication,
                       @RequestParam(value = "error", required = false) String error,
                       Model model,
                       HttpServletResponse response) throws IOException {

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            // Đã đăng nhập => Redirect theo vai trò
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                switch (role) {
                    case "ROLE_ADMIN":
                        response.sendRedirect("/admin");
                        return null;
                    case "ROLE_DEPARTMENT_HEAD":
                        response.sendRedirect("/department-head");
                        return null;
                    case "ROLE_DOCTOR":
                        response.sendRedirect("/doctor");
                        return null;
                    case "ROLE_NURSE":
                        response.sendRedirect("/nurse");
                        return null;
                    case "ROLE_RECEPTIONIST":
                        response.sendRedirect("/receptionist");
                        return null;
                    case "ROLE_LAB_TECHNICIAN":
                        response.sendRedirect("/lab-technician");
                        return null;
                    case "ROLE_CASHIER":
                        response.sendRedirect("/cashier");
                        return null;
                    case "ROLE_PHARMACY_STAFF":
                        response.sendRedirect("/pharmacy");
                        return null;
                    case "ROLE_PATIENT":
                        response.sendRedirect("/patient");
                        return null;
                }
            }
        }

        // Nếu có lỗi đăng nhập, thêm vào model để hiển thị toast
        if (error != null) {
            model.addAttribute("loginError", true);
        }

        return "index"; // Hiển thị trang chủ (chứa login modal)
    }

}