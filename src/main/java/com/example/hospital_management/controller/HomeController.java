package com.example.hospital_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        model.addAttribute("maxDate", LocalDate.now().plusDays(7));
        if (error != null) {
            model.addAttribute("loginError", "Email hoặc mật khẩu không đúng!");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Đăng xuất thành công!");
        }

        return "index";
    }

}