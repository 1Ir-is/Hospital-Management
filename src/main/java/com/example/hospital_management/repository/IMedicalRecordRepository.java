package com.example.hospital_management.repository;

import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    //Để khi xóa tránh trùng hơn, nếu dùng count xóa sẽ trùng
    @Query("select COALESCE(MAX(m.id), 0) from MedicalRecord m")
    Long findMaxId();

    @Query("select m from MedicalRecord m where m.vitalSign is null ")
    Page<MedicalRecord> findAllWithOutVitalSign(Pageable pageable);

}
