package com.example.hospital_management.repository;

import com.example.hospital_management.entity.TreatmentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITreatmentTaskRepository extends JpaRepository<TreatmentTask,Long> {
    @Query(value="select tt.* from  treatment_tasks tt join inpatient_treatments it  on tt.inpatient_treatment_id = it.id " +
            "where tt.inpatient_treatment_id = :treatmentTaskId ",nativeQuery = true)
    List<TreatmentTask> getAllTreatmentTaskByInpatientTreatmentId(@Param("treatmentTaskId")Long treatmentTaskId);
}
