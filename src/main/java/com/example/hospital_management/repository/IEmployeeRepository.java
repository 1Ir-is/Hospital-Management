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

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
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
}
