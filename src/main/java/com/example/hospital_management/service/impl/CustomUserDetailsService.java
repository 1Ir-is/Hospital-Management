package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.EmployeeRole;
import com.example.hospital_management.repository.IEmployeeRepository;
import com.example.hospital_management.service.IEmployeeRoleRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private IEmployeeRoleRService employeeRoleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {
            throw new UsernameNotFoundException("Không tìm thấy nhân viên với email: " + email);
        }

        if (employee.getStatus() == null || !employee.getStatus()) {
            throw new UsernameNotFoundException("Tài khoản đã bị vô hiệu hóa: " + email);
        }

        // Lấy danh sách vai trò của nhân viên
        List<EmployeeRole> employeeRoles = employeeRoleService.findByEmployeeId(employee.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (EmployeeRole employeeRole : employeeRoles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + employeeRole.getRole().getCode()));
        }

        if (authorities.isEmpty()) {
            throw new UsernameNotFoundException("Nhân viên chưa được phân quyền: " + email);
        }

        return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}