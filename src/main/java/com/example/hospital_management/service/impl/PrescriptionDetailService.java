package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.PrescriptionDetailDto;
import com.example.hospital_management.repository.IPrescriptionDetailRepository;
import com.example.hospital_management.entity.PrescriptionDetail;
import com.example.hospital_management.repository.IPrescriptionDetailRepository;
import com.example.hospital_management.service.IPrescriptionDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionDetailService implements IPrescriptionDetailService {
    private final IPrescriptionDetailRepository prescriptionDetailRepository;

    public PrescriptionDetailService(IPrescriptionDetailRepository prescriptionDetailRepository) {
        this.prescriptionDetailRepository = prescriptionDetailRepository;
    }


    @Override
    public List<PrescriptionDetailDto> getDetailsByPrescriptionId(Long prescriptionId) {
        return prescriptionDetailRepository.getDetailsByPrescriptionId(prescriptionId);
    }
    @Override
    public void save(PrescriptionDetail detail) {
        prescriptionDetailRepository.save(detail);
    }
}
