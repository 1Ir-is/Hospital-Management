package com.example.hospital_management.dto;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.ImpatientRecord;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;


@Getter
@Setter
public class AdvancePaymentDto implements Validator {
    private Long fee;
    private LocalDate date;
    private String reason;

    @ManyToOne
    private ImpatientRecord impatientRecord;

    @ManyToOne
    private Employee employee;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        AdvancePaymentDto advancePaymentDto = (AdvancePaymentDto) target;
        if (advancePaymentDto.getFee()==null) {
            errors.rejectValue("fee", "Empty", "Không được để trống");
        }else if(advancePaymentDto.getFee() <=50000){
            errors.rejectValue("fee","Invalid","Số tiền phải lớn hơn 50.000 đồng");
        }

        if(advancePaymentDto.getReason() == null || advancePaymentDto.getReason().isBlank()){
            errors.rejectValue("reason","Empty","Không được để trống lý do");
        }
        if(advancePaymentDto.getDate()==null){
            errors.rejectValue("date", "Empty", "Không được để trống");
        }else if(advancePaymentDto.getDate().isAfter(LocalDate.now())){
            errors.rejectValue("date","Invalid","Ngày tạm ứng không được vượt quá hôm nay!!");
        }

//        if (advancePaymentDto.impatientRecord.getId() == null) {
//            errors.rejectValue("impatientRecordId", "Empty", "Phải chọn hồ sơ bệnh án nội trú");
//        } else if (!impatientRecordService.existsById(advancePaymentDto.impatientRecord.getId())) {
//            errors.rejectValue("impatientRecordId", "Invalid", "Hồ sơ bệnh án không tồn tại");
//        }

//        if (dto.getEmployeeId() == null) {
//            errors.rejectValue("employeeId", "Empty", "Phải chọn nhân viên");
//        } else if (!employeeService.existsById(dto.getEmployeeId())) {
//            errors.rejectValue("employeeId", "Invalid", "Nhân viên không tồn tại");
//        }

    }
}
