package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment_Id(Long departmentId);
    Optional<Employee> findByEmail(String email); // dùng cho login



    @Query("SELECT e FROM Employee e WHERE e.position.name = 'Bác sĩ'")
    List<Employee> findAllDoctors();
    @Query("SELECT e FROM Employee e WHERE e.position.name = 'Điều dưỡng'")
    List<Employee> findAllNurses();
    @Query("SELECT e FROM Employee e WHERE e.position.name = 'Bác sĩ' AND e.department.id = :departmentId")
    List<Employee> findDoctorsByDepartment(@Param("departmentId") Long departmentId);

    @Query("SELECT e FROM Employee e WHERE e.position.name = 'Điều dưỡng' AND e.department.id = :departmentId")
    List<Employee> findNursesByDepartment(@Param("departmentId") Long departmentId);

}
