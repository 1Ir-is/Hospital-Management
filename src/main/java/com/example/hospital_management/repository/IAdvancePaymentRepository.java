package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;


public interface IAdvancePaymentRepository extends JpaRepository<AdvancePayment, Long> {

    @Query(value="select ap.* from advance_payments ap " +
            "join impatient_records ir on ap.impatient_record_id = ir.id " +
            "join employees e on ap.employee_id = e.id " +
            "JOIN medical_records mr on ir.medical_record_id = mr.id " +
            "join patients p on mr.patient_id = p.id " +
            "where p.name like concat('%', :patientName ,'%') and  e.id= :employeeId",

            countQuery="select count(*) from advance_payments ap " +
                    "join impatient_records ir on ap.impatient_record_id = ir.id " +
                    "join employees e on ap.employee_id = e.id " +
                    "JOIN medical_records mr on ir.medical_record_id = mr.id " +
                    "join patients p on mr.patient_id = p.id " +
                    "where p.name like concat('%', :patientName,'%') and  e.id= :employeeId "

            ,nativeQuery = true)
    Page<AdvancePayment> findAdvancePaymentByPatientNameAndEmployeeId(@Param("patientName") String patientName,
                                                                      @Param("employeeId") Long employeeId, Pageable pageable);
}
