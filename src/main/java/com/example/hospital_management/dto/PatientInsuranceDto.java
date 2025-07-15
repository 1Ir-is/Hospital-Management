package com.example.hospital_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientInsuranceDto implements Validator {
    //Patient
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Boolean gender;
    private String address;
    private String phone;
    private String email;
    private String idCard;
    private Boolean hasInsurance;
    private Integer queueNumber;

    //Insurance
    private String code;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;
    private String registeredClinic;

    private Long departmentId;
    private Long roomId;
    private Long examinationShiftId;
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        PatientInsuranceDto patientInsuranceDto = (PatientInsuranceDto) target;

        if (patientInsuranceDto.getName().trim().isEmpty()) {
            errors.rejectValue("name", "null", "Tên không được để trống");
        } else if (!patientInsuranceDto.getName().matches("^[\\p{L} ]{2,100}$")) {
            errors.rejectValue("name", "patient.name.invalid", "Họ tên chỉ được chứa chữ cái và khoảng trắng, không chứa số hoặc ký tự đặc biệt");
        }

        if (patientInsuranceDto.getBirthday() == null) {
            errors.rejectValue("birthday","null","Sinh nhật không được để trống");
        } else if (patientInsuranceDto.getBirthday().isAfter(LocalDate.now())) {
            errors.rejectValue("birthday","null","Ngày sinh không được lớn hơn ngày hiện tại");
        }

        if (patientInsuranceDto.getGender() == null) {
            errors.rejectValue("gender","null","Giới tính không được để trống");
        }

        if (patientInsuranceDto.getAddress() == null) {
            errors.rejectValue("address","null","Địa chỉ không được để trống");
        }

        if (patientInsuranceDto.getPhone() == null) {
            errors.rejectValue("phone","null","Số điện thoại không được để trống");
        } else if (!patientInsuranceDto.getPhone().matches("^(0|\\+84)[0-9]{9}$")) {
            errors.rejectValue("phone","null","Số điện thoại không đúng định dạng. Hợp lệ: 0905112112");
        }

        if (patientInsuranceDto.getEmail() == null) {
            errors.rejectValue("email","null","Email không được để trống");
        } else if (!patientInsuranceDto.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            errors.rejectValue("email","null","Email không đúng định dạng");
        }

        if (patientInsuranceDto.getIdCard() == null) {
            errors.rejectValue("idCard","null", "CCCD không được để trống");
        } else if (!patientInsuranceDto.getIdCard().matches("^\\d{12}$")) {
            errors.rejectValue("idCard","null","CCCD không đúng định dạng");
        }

        // Validate BHYT nếu người dùng khai báo có
        if (Boolean.TRUE.equals(patientInsuranceDto.getHasInsurance())) {
            if (patientInsuranceDto.getCode() == null || patientInsuranceDto.getCode().trim().isEmpty()) {
                errors.rejectValue("code", "null", "Mã BHYT không được để trống");
            } else if (!patientInsuranceDto.getCode().matches("^\\d{10}$")) {
                errors.rejectValue("code", "invalid", "Mã BHYT phải đủ 10 số");
            }

            if (patientInsuranceDto.getEffectiveDate() == null) {
                errors.rejectValue("effectiveDate", "null", "Ngày hiệu lực không được để trống");
            }

            if (patientInsuranceDto.getExpiryDate() == null) {
                errors.rejectValue("expiryDate", "null", "Ngày hết hạn không được để trống");
            }

            if (patientInsuranceDto.getEffectiveDate() != null && patientInsuranceDto.getExpiryDate() != null
                    && patientInsuranceDto.getEffectiveDate().isAfter(patientInsuranceDto.getExpiryDate())) {
                errors.rejectValue("effectiveDate", "invalid.date", "Ngày hiệu lực phải trước hoặc bằng ngày hết hạn");
            }

            if (patientInsuranceDto.getRegisteredClinic() == null || patientInsuranceDto.getRegisteredClinic().trim().isEmpty()) {
                errors.rejectValue("registeredClinic", "null", "Nơi đăng ký KCB không được để trống");
            }
        }

    }
}
