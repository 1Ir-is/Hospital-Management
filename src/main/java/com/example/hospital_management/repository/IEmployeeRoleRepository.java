package com.example.hospital_management.repository;

import com.example.hospital_management.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {
    @Query("SELECT er FROM EmployeeRole er WHERE er.employee.id = :employeeId")
    List<EmployeeRole> findByEmployeeId(@Param("employeeId") Long employeeId);

    @Modifying
    @Query("DELETE FROM EmployeeRole er WHERE er.employee.id = :employeeId")
    void deleteByEmployeeId(@Param("employeeId") Long employeeId);
}
