package com.example.hospital_management.controller.lab_technician;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lab-technician")
@PreAuthorize("hasAnyRole('LAB_TECHNICIAN', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class LabTechnicianController {

    @GetMapping()
    public String labTechnicianDashboard(Model model) {
        // LAB_TECHNICIAN - Chức năng do Nhơn làm:
        // - Kỹ thuật viên có thể xem danh sách các bệnh nhân cần làm xét nghiệm thuộc chuyên môn của phòng mình
        // - Kỹ thuật viên có thể thực hiện xét các xét nghiệm và lưu thông tin kết quả cho người khám
        // - Kỹ thuật viên có thể xem tất cả các xét nghiệm của bệnh nhân và hướng dẫn họ thực hiện các bước xét nghiệm tiếp theo

        model.addAttribute("pageTitle", "Kỹ thuật viên xét nghiệm");
        model.addAttribute("userRole", "Kỹ thuật viên");
        return "lab-technician/index";
    }
}