//package com.example.hospital_management.controller.receptionist;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/receptionist")
//@PreAuthorize("hasAnyRole('RECEPTIONIST', 'NURSE', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
//public class ReceptionistController {
//
//    @GetMapping()
//    public String receptionistDashboard(Model model) {
//        // RECEPTIONIST - Chức năng do Bình làm:
//        // - Lễ tân khoa khám có thể nhập thông tin của bệnh nhân (có bảo hiểm y tế hoặc không có)
//        // - Lễ tân khoa khám có thể chỉ định phòng khám cho từng bệnh nhân
//        // - Lễ tân khoa khám có thể biết được tiến trình khám của bệnh nhân (đang chờ, đang khám, đã khám xong)
//        // - Lễ tân khoa khám có thể xem chi tiết các xét nghiệm của một bệnh nhân
//
//        model.addAttribute("pageTitle", "Lễ tân");
//        model.addAttribute("userRole", "Lễ tân");
//        return "receptionist/index";
//    }
//}