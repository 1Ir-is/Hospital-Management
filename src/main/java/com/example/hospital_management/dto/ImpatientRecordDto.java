package com.example.hospital_management.dto;

import lombok.AllArgsConstructor;
import com.example.hospital_management.entity.Bed;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImpatientRecordDto implements Validator {
    private LocalDate admissionDate; //Ngày nhập viện
    private LocalDate dischargeDate; //Ngày xuất viện
    private Boolean status;
    private String note;
    private String reason;
    private Bed bed;

    private MedicalRecord medicalRecord;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ImpatientRecordDto  record = (ImpatientRecordDto) target;

        if(record.getAdmissionDate() == null){
            errors.rejectValue("admissionDate","admissionDate.null","Ngày nhập viện không được để trống");
        }

        if(record.getAdmissionDate() !=null && record.getDischargeDate()!=null){
            if(record.getDischargeDate().isBefore(record.getAdmissionDate())){
                errors.rejectValue("dischargeDate","dischargeDate.invalid","Ngày xuất viện phải sau hoặc bằng ngày nhập viện");
            }
        }

        if(record.getNote()==null || record.getNote().trim().isEmpty()){
            errors.rejectValue("note","note.null","Ghi chú không được để trống");
        }else if(record.getNote().length()>500){
            errors.rejectValue("note","note.long","Ghi chú không được quá 500 ký tự");
        }
        if(record.getReason()==null || record.getReason().trim().isEmpty()){
            errors.rejectValue("reason","reason.null","Lý do không được để trống");
        }

        if(record.getBed()==null||record.getBed().getId()==null){
            errors.rejectValue("bed","bed.null","Giường không được để trống");
        }
    }
}



