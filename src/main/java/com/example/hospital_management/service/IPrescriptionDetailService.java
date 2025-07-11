package com.example.hospital_management.service;

import com.example.hospital_management.dto.PrescriptionDetailDto;
import java.util.List;


import com.example.hospital_management.entity.PrescriptionDetail;

public interface IPrescriptionDetailService {
   List<PrescriptionDetailDto> getDetailsByPrescriptionId(Long prescriptionId);
    void save(PrescriptionDetail detail);
}
