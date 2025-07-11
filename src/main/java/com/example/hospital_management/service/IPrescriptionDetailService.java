package com.example.hospital_management.service;

import com.example.hospital_management.dto.PrescriptionDetailDto;
import java.util.List;


public interface IPrescriptionDetailService {
   List<PrescriptionDetailDto> getDetailsByPrescriptionId(Long prescriptionId);
}
