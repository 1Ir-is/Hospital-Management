package com.example.hospital_management.repository;

import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;


public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    @Query(value = "SELECT mr FROM MedicalRecord mr WHERE mr.code = :code")
    Optional<MedicalRecord> findRoomByCode(@Param("code") String code);

    Optional<MedicalRecord> findDistinctByPatient_IdCard(String patientIdCard);


    List<MedicalRecord> findByPatient(Patient patient);

    Optional<MedicalRecord> findByCode(String code);
}
