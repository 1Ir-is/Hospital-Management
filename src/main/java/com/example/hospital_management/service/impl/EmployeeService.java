package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.repository.IEmployeeRepository;
import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.EmployeeRole;
import com.example.hospital_management.entity.Role;
import com.example.hospital_management.repository.IEmployeeRepository;
import com.example.hospital_management.repository.IEmployeeRoleRepository;
import com.example.hospital_management.repository.IRoleRepository;
import com.example.hospital_management.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private IEmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Employee> findByDepartment(Long departmentId) {
        return employeeRepository.findByDepartment_Id(departmentId);
    }

    @Override
    public List<Employee> findAllDoctors() {
        return employeeRepository.findAllDoctors();
    }

    @Override
    public List<Employee> findAllNurses() {
        return employeeRepository.findAllNurses();
    }

    @Override
    public List<Employee> findDoctorsByDepartment(Long departmentId) {
        return employeeRepository.findDoctorsByDepartment(departmentId);
    }

    @Override
    public List<Employee> findNursesByDepartment(Long departmentId) {
        return employeeRepository.findNursesByDepartment(departmentId);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Page<Employee> findEmployeesWithFilters(String search, String status, Pageable pageable) {
        return employeeRepository.findEmployeesWithFilters(search, status, pageable);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        if (employee.getPassword() != null && !employee.getPassword().isEmpty()) {
            // Kiểm tra xem password đã được mã hóa chưa (BCrypt hash bắt đầu bằng $2a$, $2b$, $2y$)
            if (!employee.getPassword().startsWith("$2a$") &&
                    !employee.getPassword().startsWith("$2b$") &&
                    !employee.getPassword().startsWith("$2y$")) {
                employee.setPassword(passwordEncoder.encode(employee.getPassword()));
            }
        }

        if (employee.getStartingDate() == null) {
            employee.setStartingDate(LocalDate.now());
        }
        if (employee.getStatus() == null) {
            employee.setStatus(true);
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee findEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.orElse(null);
    }

//    @Override
//    public Employee findEmployeeById(Long id) {
//        return employeeRepository.findEmployeeById(id);
//    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }


    @Override
    @Transactional
    public void saveEmployeeWithRoles(Employee employee, List<Long> roleIds) {
        // ✅ Bây giờ saveEmployee() đã có logic mã hóa password
        Employee savedEmployee = saveEmployee(employee);

        // Lưu roles cho employee
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                Role role = roleRepository.findById(roleId).orElse(null);
                if (role != null) {
                    EmployeeRole employeeRole = new EmployeeRole();
                    employeeRole.setEmployee(savedEmployee);
                    employeeRole.setRole(role);
                    employeeRoleRepository.save(employeeRole);
                }
            }
        }
    }


    @Override
    @Transactional
    public void updateEmployeeRoles(Long employeeId, List<Long> roleIds) {
        // Xoa tat ca role cu
        employeeRoleRepository.deleteByEmployeeId(employeeId);

        // Them role moi
        Employee employee = findEmployeeById(employeeId);
        if (employee != null && roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                Role role = roleRepository.findById(roleId).orElse(null);
                if (role != null) {
                    EmployeeRole employeeRole = new EmployeeRole();
                    employeeRole.setEmployee(employee);
                    employeeRole.setRole(role);
                    employeeRoleRepository.save(employeeRole);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateEmployeeWithRoles(Long employeeId, Employee updatedEmployee, List<Long> roleIds) {
        Employee existingEmployee = findEmployeeById(employeeId);
        if (existingEmployee == null) {
            throw new RuntimeException("Employee not found with id: " + employeeId);
        }

        // Update thông tin employee
        existingEmployee.setName(updatedEmployee.getName());
        existingEmployee.setPhone(updatedEmployee.getPhone());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setGender(updatedEmployee.getGender());
        existingEmployee.setAddress(updatedEmployee.getAddress());
        existingEmployee.setStartingDate(updatedEmployee.getStartingDate());
        existingEmployee.setStatus(updatedEmployee.getStatus());
        existingEmployee.setAvatar(updatedEmployee.getAvatar());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setPosition(updatedEmployee.getPosition());

        if (updatedEmployee.getPassword() != null && !updatedEmployee.getPassword().trim().isEmpty()) {
            existingEmployee.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
        }
        // Nếu password trống thì giữ nguyên password cũ

        // Save employee
        employeeRepository.save(existingEmployee);

        // Update roles
        updateEmployeeRoles(employeeId, roleIds);
    }

    @Override
    public Optional<Employee> findEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public long countTotalEmployees() {
        return employeeRepository.count();
    }

    @Override
    public long countActiveEmployees() {
        return employeeRepository.countByStatus(true);
    }

    @Override
    public long countInactiveEmployees() {
        return employeeRepository.countByStatusOrStatusIsNull(false);
    }

    @Override
    public long countNewEmployeesThisMonth() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        return employeeRepository.countByStartingDateGreaterThanEqual(startOfMonth);
    }
}