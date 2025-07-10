package com.example.hospital_management.controller.patient;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient")
@PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'NURSE', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class PatientController {

    @GetMapping()
    public String patientDashboard(Model model) {
        // PATIENT - Chức năng do Duy làm:
        // - Người khám bệnh có thể lấy số phiếu
        // - Người khám bệnh có thể đặt lịch khám trước ngày khám
        // - Người khám bệnh có thể xem được phòng mình sẽ được khám
        // - Người khám bệnh có thể xem được các kết quả xét nghiệm
        // - Người khám bệnh có thể xem được kết quả sau khi khám
        // - Người khám bệnh có thể biết được vị trí các phòng xét nghiệm mà bác sĩ chỉ định
        // - Người khám có thể nhận được mail thông báo ngày tái khám trước 3 ngày theo yêu cầu bác sĩ

        model.addAttribute("pageTitle", "Bệnh nhân");
        model.addAttribute("userRole", "Bệnh nhân");
        return "patient/index";
    }
}