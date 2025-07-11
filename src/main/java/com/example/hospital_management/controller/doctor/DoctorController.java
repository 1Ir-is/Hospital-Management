//package com.example.hospital_management.controller.doctor;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/doctor")
//@PreAuthorize("hasAnyRole('DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
//public class DoctorController {
//
//    @GetMapping()
//    public String doctorDashboard(Model model) {
//        // DOCTOR - Chức năng do Vĩnh và Chung làm:
//        // Vĩnh (Bác sĩ khám):
//        // - Khám tổng quát và chỉ định các xét nghiệm
//        // - Biết tiến độ thực hiện các kết quả xét nghiệm cho bệnh nhân
//        // - Xem các xét nghiệm và đưa ra kết quả và cách điều trị cho bệnh nhân
//        // - Kê thuốc cho bệnh nhân sau khi khám nếu không phải nhập viện
//        // - Yêu cầu bệnh nhân nhập viện nếu tình trạng cấp bách
//
//        // Chung (Bác sĩ điều trị):
//        // - Chỉ định các xét nghiệm và có thể phẫu thuật cho bệnh nhân
//        // - Xem các kết quả xét nghiệm của từng bệnh nhân
//        // - Kê thuốc điều trị hàng ngày cho bệnh nhân
//        // - Đưa ra kết luận bệnh nhân được xuất viện
//
//        model.addAttribute("pageTitle", "Bác sĩ");
//        model.addAttribute("userRole", "Bác sĩ");
//        return "doctor/index";
//    }
//}