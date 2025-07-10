package com.example.hospital_management.controller.departmenthead;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/department-head")
@PreAuthorize("hasAnyRole('DEPARTMENT_HEAD', 'ADMIN')")
public class DepartmentHeadController {

    @GetMapping()
    public String showDepartmentHeadDashboard(Model model) {
        // DEPARTMENT_HEAD - Chức năng do Vương làm:
        // - Phân công bác sĩ phụ trách điều trị cho bệnh nhân
        // - Xem danh sách nhân viên trong khoa
        // - Phân công các điều dưỡng phụ trách mỗi phòng của bệnh nhân
        // - Phân công bác sĩ phẫu thuật cho bệnh nhân

        model.addAttribute("pageTitle", "Quản lý khoa");
        model.addAttribute("userRole", "Trưởng khoa");
        return "department-head/index";
    }
}