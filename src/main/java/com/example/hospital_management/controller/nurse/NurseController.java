package com.example.hospital_management.controller.nurse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nurse")
@PreAuthorize("hasAnyRole('NURSE', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class NurseController {

    @GetMapping()
    public String nurseDashboard(Model model) {
        // NURSE - Chức năng do Khánh làm:
        // - Điều dưỡng có thể xem danh sách các bệnh án do mình phụ trách
        // - Điều dưỡng phụ trách phải thực hiện cấp phát thuốc và làm các công việc hàng ngày mà bác sĩ đã chỉ định cho từng bệnh nhân
        // - Điều dưỡng có thể làm yêu cầu tạm ứng viện phí cho bệnh nhân
        // - Điều dưỡng có thể làm thủ tục xuất viện cho bệnh nhân
        // - Lễ tân của khoa có thể làm hồ sơ nhập viện cho bệnh nhân (chọn phòng và giường mà bệnh nhân sẽ nằm)

        // NURSE - Chức năng do Bình làm:
        // - Y tá khoa khám có thể kiểm tra huyết áp, cân nặng của bệnh nhân

        model.addAttribute("pageTitle", "Điều dưỡng");
        model.addAttribute("userRole", "Điều dưỡng");
        return "nurse/index";
    }
}