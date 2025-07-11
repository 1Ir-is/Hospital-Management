package com.example.hospital_management.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class TestOrderDTO {
    private Long id;
    @NotNull(message = "Ngày không được để trống")
    private LocalTime date; // Sửa thành LocalDateTime để lưu ngày giờ
    private String note;
    @NotNull(message = "Trạng thái không được để trống")
    private Boolean status;
    private String result;
    @NotNull(message = "Bệnh nhân không được để trống")
    private Long impatientRecordId;
    @NotNull(message = "Nhân viên không được để trống")
    private Long employeeId;
    @NotEmpty(message = "Phải chọn ít nhất một xét nghiệm")
    private List<Long> testIds; // Danh sách ID từ checkbox

}
