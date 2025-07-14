package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByDepartment_Id(Long departmentId);

    @Query("SELECT e FROM Employee e WHERE e.position.name = 'Bác sĩ'")
    List<Employee> findAllDoctors();

    @Query("SELECT e FROM Employee e WHERE e.position.name = 'Điều dưỡng'")
    List<Employee> findAllNurses();

    @Query("SELECT e FROM Employee e WHERE e.position.name = 'Bác sĩ' AND e.department.id = :departmentId")
    List<Employee> findDoctorsByDepartment(@Param("departmentId") Long departmentId);

    @Query("SELECT e FROM Employee e WHERE e.position.name = 'Điều dưỡng' AND e.department.id = :departmentId")
    List<Employee> findNursesByDepartment(@Param("departmentId") Long departmentId);

    List<Employee> findByDepartmentId(Long departmentId);

    @Query("SELECT e FROM Employee e WHERE e.email = :email")
    Employee findByEmail(@Param("email") String email);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:search = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.phone) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.department.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.position.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:status = '' OR " +
            "(:status = 'active' AND e.status = true) OR " +
            "(:status = 'inactive' AND (e.status = false OR e.status IS NULL)))")
    Page<Employee> findEmployeesWithFilters(@Param("search") String search,
                                            @Param("status") String status,
                                            Pageable pageable);

    // ✅ New count methods for statistics
    long countByStatus(Boolean status);

    Employee findEmployeeById(Long id);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.status = false OR e.status IS NULL")
    long countByStatusOrStatusIsNull(Boolean status);

    long countByStartingDateGreaterThanEqual(LocalDate startDate);

    Optional<Employee> findEmployeeByEmail(String email);


    @Query(value = "select e.* from employees e join employee_roles er on e.id = er.employee_id " +
            "join roles r on er.roles_id = r.id where r.id = 4; ", nativeQuery = true)
    List<Employee> getListNurse();
}
