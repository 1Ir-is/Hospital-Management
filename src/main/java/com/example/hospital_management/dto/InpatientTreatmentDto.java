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

        if(inpatientTreatmentDto.getTreatmentLocation()==null){
            errors.rejectValue("treatmentLocation","treatmentLocation.null","Không được để nơi điều trị là rỗng");
        }

    }
}
