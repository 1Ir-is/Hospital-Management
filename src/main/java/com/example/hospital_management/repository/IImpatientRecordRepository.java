package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.ImpatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImpatientRecordRepository extends JpaRepository<ImpatientRecord, Long> {
}
