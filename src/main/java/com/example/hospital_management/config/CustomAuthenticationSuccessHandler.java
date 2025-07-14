package com.example.hospital_management.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectURL = "/";

        // Điều hướng dựa trên vai trò cao nhất
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                // ADMIN - Chức năng do Huy làm
                // - Thêm mới nhân viên bệnh viện
                // - Thêm mới khoa khám và điều trị
                // - Xem danh sách phòng khám
                // - Điều chuyển nhân viên giữa các khoa
                // - Xem danh sách ca khám hiện tại
                // - Phân công bác sĩ phụ trách phòng khám theo lịch
                // - Phân quyền cho toàn bộ hệ thống
                redirectURL = "/admin";
                break;
            } else if (role.equals("ROLE_DEPARTMENT_HEAD")) {
                // DEPARTMENT_HEAD - Chức năng do Vương làm
                // - Phân công bác sĩ phụ trách điều trị cho bệnh nhân
                // - Xem danh sách nhân viên trong khoa
                // - Phân công điều dưỡng phụ trách mỗi phòng bệnh nhân
                // - Phân công bác sĩ phẫu thuật cho bệnh nhân
                redirectURL = "/department-head/dashboard";
                break;
            } else if (role.equals("ROLE_DOCTOR")) {
                // DOCTOR - Chức năng do Vĩnh và Chung làm
                // Vĩnh (Bác sĩ khám):
                // - Khám tổng quát và chỉ định xét nghiệm
                // - Biết tiến độ thực hiện kết quả xét nghiệm
                // - Xem xét nghiệm và đưa ra kết quả điều trị
                // - Kê thuốc cho bệnh nhân sau khám
                // - Yêu cầu bệnh nhân nhập viện nếu cấp bách
                // Chung (Bác sĩ điều trị):
                // - Chỉ định xét nghiệm và phẫu thuật cho bệnh nhân nội trú
                // - Xem kết quả xét nghiệm của từng bệnh nhân
                // - Kê thuốc điều trị hàng ngày
                // - Đưa ra kết luận bệnh nhân được xuất viện
                redirectURL = "/doctor";
                break;
            } else if (role.equals("ROLE_NURSE")) {
                // NURSE - Chức năng do Khánh làm
                // - Kiểm tra huyết áp, cân nặng bệnh nhân
                // - Xem danh sách bệnh án do mình phụ trách
                // - Cấp phát thuốc và làm công việc hàng ngày theo chỉ định bác sĩ
                // - Làm yêu cầu tạm ứng viện phí cho bệnh nhân
                // - Làm thủ tục xuất viện cho bệnh nhân
                // - Làm hồ sơ nhập viện (chọn phòng và giường)
                redirectURL = "/nurse";
                break;
            } else if (role.equals("ROLE_RECEPTIONIST")) {
                // RECEPTIONIST - Chức năng do Bình làm
                // - Nhập thông tin bệnh nhân (có/không bảo hiểm y tế)
                // - Chỉ định phòng khám cho từng bệnh nhân
                // - Biết tiến trình khám bệnh nhân (đang chờ, đang khám, đã khám xong)
                // - Xem chi tiết xét nghiệm của bệnh nhân
                redirectURL = "/receptionist";
                break;
            } else if (role.equals("ROLE_LAB_TECHNICIAN")) {
                // LAB_TECHNICIAN - Chức năng do Nhơn làm
                // - Xem danh sách bệnh nhân cần làm xét nghiệm thuộc chuyên môn phòng mình
                // - Thực hiện xét nghiệm và lưu kết quả cho người khám
                // - Xem tất cả xét nghiệm của bệnh nhân và hướng dẫn thực hiện các bước tiếp theo
                redirectURL = "/lab-technician";
                break;
            } else if (role.equals("ROLE_CASHIER")) {
                // CASHIER - Chức năng do Nhơn làm
                // - Thu tiền khám, xét nghiệm, thuốc
                redirectURL = "/cashier";
                break;
            } else if (role.equals("ROLE_PHARMACY_STAFF")) {
                // PHARMACY_STAFF - Chức năng do Nhơn làm
                // - Cấp thuốc cho bệnh nhân theo đơn thuốc điều trị của bác sĩ
                redirectURL = "/pharmacy";
                break;
            } else if (role.equals("ROLE_PATIENT")) {
                // PATIENT - Chức năng do Duy làm
                // - Lấy số phiếu khám
                // - Đặt lịch khám trước ngày khám
                // - Xem được phòng mình sẽ được khám
                // - Xem được các kết quả xét nghiệm
                // - Xem được kết quả sau khi khám
                // - Biết được vị trí các phòng xét nghiệm mà bác sĩ chỉ định
                // - Nhận mail thông báo ngày tái khám trước 3 ngày theo yêu cầu bác sĩ
                redirectURL = "/patient";
                break;
            }
        }

        response.sendRedirect(redirectURL);
    }
}