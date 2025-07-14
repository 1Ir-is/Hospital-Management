package com.example.hospital_management.service;

import com.example.hospital_management.entity.InpatientTreatment;

import java.util.List;

public interface IInpatientTreatmentService {
    void save(InpatientTreatment inpatientTreatment);
    List<InpatientTreatment> findByImpatientRecordId(Long recordId);
}
