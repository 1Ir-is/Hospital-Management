package com.example.hospital_management.controller.cashier;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cashier")
@PreAuthorize("hasAnyRole('CASHIER', 'RECEPTIONIST', 'NURSE', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class CashierController {

    @GetMapping()
    public String cashierDashboard(Model model) {
        // CASHIER - Chức năng do Nhơn làm:
        // - Nhân viên thu ngân có thể thu tiền khám, xét nghiệm, thuốc

        model.addAttribute("pageTitle", "Thu ngân");
        model.addAttribute("userRole", "Thu ngân");
        return "cashier/index";
    }
}