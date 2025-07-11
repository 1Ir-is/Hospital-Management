//package com.example.hospital_management.controller.pharmacy;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/pharmacy")
//@PreAuthorize("hasAnyRole('PHARMACY_STAFF', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
//public class PharmacyController {
//
//    @GetMapping()
//    public String pharmacyDashboard(Model model) {
//        // PHARMACY_STAFF - Chức năng do Nhơn làm:
//        // - Nhân viên phòng vật tư và thiết bị y tế có thể cấp thuốc cho bệnh nhân theo đơn thuốc điều trị của bác sĩ
//
//        model.addAttribute("pageTitle", "Phòng vật tư");
//        model.addAttribute("userRole", "Nhân viên phòng vật tư");
//        return "pharmacy/index";
//    }
//}