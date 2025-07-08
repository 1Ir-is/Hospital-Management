package com.example.hospital_management.service;

import com.example.hospital_management.entity.Role;

import java.util.List;

public interface IRoleService {
    List<Role> findAllRoles();

    Role saveRole(Role role);

    Role findRoleById(Long id);

    Role findRoleByCode(String code);

    void deleteRoleById(Long id);
}
