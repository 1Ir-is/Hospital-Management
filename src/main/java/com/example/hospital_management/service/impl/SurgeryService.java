package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Surgery;
import com.example.hospital_management.repository.ISurgeryRepository;
import com.example.hospital_management.service.ISurgeryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurgeryService implements ISurgeryService {
    private ISurgeryRepository surgeryRepository;
    @Autowired
    public SurgeryService(ISurgeryRepository surgeryRepository) {
        this.surgeryRepository = surgeryRepository;
    }


    @Override
    public List<Surgery> findAll() {
        return surgeryRepository.findAll();
    }

    @Override
    public Page<Surgery> findAll(Pageable pageable) {
        return surgeryRepository.findAll(pageable);
    }

    @Override
    public void save(Surgery surgery) {
        surgeryRepository.save(surgery);
    }

    @Override
    public Optional<Surgery> findById(Long id) {
        return surgeryRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        surgeryRepository.deleteById(id);
    }

    @Override
    public Page<Surgery> searchByName(String searchByName, Long medicalRecordId, Pageable pageable) {
        return surgeryRepository.searchByName(searchByName,medicalRecordId,pageable);
    }
}
