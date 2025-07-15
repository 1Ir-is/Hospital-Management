package com.example.hospital_management.dto;

import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.Medicine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Getter
@Setter
public class InpatientTreatmentDto implements Validator {
    private int quantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer morningDose;
    private Integer eveningDose;
    private boolean status;
    private String note;
    private String treatmentLocation;

    private ImpatientRecord impatientRecord;
    private Medicine medicine;
    private Integer estimateNumberOfDate;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        InpatientTreatmentDto inpatientTreatmentDto = (InpatientTreatmentDto) target;

        if(inpatientTreatmentDto.getStartDate() == null){
            errors.rejectValue("startDate","startDate.null","Không được để ngày là rỗng");
        }

        if(inpatientTreatmentDto.getTreatmentLocation().isEmpty()){
            errors.rejectValue("treatmentLocation","treatmentLocation.null","Không được để nơi điều trị là rỗng");
        }

        if (inpatientTreatmentDto.getMedicine() == null || inpatientTreatmentDto.getMedicine().getId() == null) {
            errors.rejectValue("medicine.id", "medicine.id.null", "Phải chọn loại thuốc");
        }

        if (inpatientTreatmentDto.getEstimateNumberOfDate() == null || inpatientTreatmentDto.getEstimateNumberOfDate() <= 0) {
            errors.rejectValue("estimateNumberOfDate", "estimateNumberOfDate.invalid", "Số ngày điều trị phải lớn hơn 0");
        }
        if (inpatientTreatmentDto.getMorningDose() == null || inpatientTreatmentDto.getMorningDose() <= 0) {
            errors.rejectValue("morningDose", "morningDose.invalid", "Liều sáng phải lớn hơn 0");
        }
        if (inpatientTreatmentDto.getEveningDose() == null || inpatientTreatmentDto.getEveningDose() <= 0) {
            errors.rejectValue("eveningDose", "eveningDose.invalid", "Liều tối phải lớn hơn 0");
        }
    }
}
