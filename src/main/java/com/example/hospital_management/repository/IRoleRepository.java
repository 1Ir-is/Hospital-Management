package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.code = :code")
    Role findByCode(@Param("code") String code);
}
