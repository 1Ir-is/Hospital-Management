package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.*;
import com.example.hospital_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminInitializationService implements ApplicationRunner {

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IEmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private IDepartmentRepository departmentRepository;

    @Autowired
    private IPositionRepository positionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("🚀 Initializing Hospital Management System...");

        try {
            // 1. Tạo các Role cơ bản nếu chưa có
            initializeRoles();

            // 2. Tạo Department và Position cơ bản nếu chưa có
            initializeDepartmentsAndPositions();

            // 3. Tạo Admin account nếu chưa có
            createDefaultAdminAccount();

            System.out.println("✅ Hospital Management System initialized successfully!");
            System.out.println("🎉 Ready to login with admin@hospital.com / admin123");

        } catch (Exception e) {
            System.err.println("❌ Error during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeRoles() {
        System.out.println("📋 Initializing roles...");

        String[] roleCodes = {"ADMIN", "DEPARTMENT_HEAD", "DOCTOR", "NURSE", "RECEPTIONIST",
                "LAB_TECHNICIAN", "CASHIER", "PHARMACY_STAFF", "PATIENT"};
        String[] roleNames = {"Quản trị viên", "Trưởng khoa", "Bác sĩ", "Điều dưỡng", "Lễ tân",
                "Kỹ thuật viên xét nghiệm", "Thu ngân", "Nhân viên phòng vật tư", "Bệnh nhân"};

        for (int i = 0; i < roleCodes.length; i++) {
            // ✅ Tạo biến final để sử dụng trong lambda
            final String currentRoleCode = roleCodes[i];
            final String currentRoleName = roleNames[i];

            try {
                // ✅ Sử dụng biến final trong lambda
                List<Role> existingRoles = roleRepository.findAll();
                Role existingRole = existingRoles.stream()
                        .filter(role -> currentRoleCode.equals(role.getCode()))
                        .findFirst()
                        .orElse(null);

                if (existingRole == null) {
                    Role role = new Role();
                    role.setCode(currentRoleCode);
                    role.setName(currentRoleName);
                    roleRepository.save(role);
                    System.out.println("   ✅ Created role: " + currentRoleName + " (" + currentRoleCode + ")");
                } else {
                    System.out.println("   ℹ️ Role already exists: " + currentRoleName);
                }
            } catch (Exception e) {
                System.err.println("   ❌ Error creating role " + currentRoleCode + ": " + e.getMessage());
            }
        }
    }

    private void initializeDepartmentsAndPositions() {
        System.out.println("🏢 Initializing departments and positions...");

        // Tạo các Department cơ bản
        String[] deptNames = {
                "Phòng Hành chính", "Khoa Nội", "Khoa Ngoại", "Khoa Sản", "Khoa Nhi",
                "Khoa Tim Mạch", "Khoa Thần Kinh", "Khoa Xét nghiệm", "Khoa Dược", "Khoa Cấp cứu"
        };
        String[] deptDescs = {
                "Phòng quản lý hành chính và điều hành chung",
                "Khoa khám và điều trị bệnh nội khoa",
                "Khoa phẫu thuật và điều trị ngoại khoa",
                "Khoa sản phụ khoa",
                "Khoa nhi đồng",
                "Khoa chuyên khoa tim mạch",
                "Khoa chuyên khoa thần kinh",
                "Khoa xét nghiệm và chẩn đoán",
                "Khoa dược và vật tư y tế",
                "Khoa cấp cứu 24/7"
        };

        // ✅ Tạo departments
        for (int i = 0; i < deptNames.length; i++) {
            final String currentDeptName = deptNames[i];
            final String currentDeptDesc = deptDescs[i];

            try {
                List<Department> existingDepts = departmentRepository.findAll();
                Department existingDept = existingDepts.stream()
                        .filter(dept -> currentDeptName.equals(dept.getName()))
                        .findFirst()
                        .orElse(null);

                if (existingDept == null) {
                    Department dept = new Department();
                    dept.setName(currentDeptName);
                    dept.setDescription(currentDeptDesc);
                    departmentRepository.save(dept);
                    System.out.println("   ✅ Created department: " + currentDeptName);
                }
            } catch (Exception e) {
                System.err.println("   ❌ Error creating department " + currentDeptName + ": " + e.getMessage());
            }
        }

        // Tạo các Position cơ bản
        String[] posNames = {
                "Quản trị viên", "Trưởng khoa", "Bác sĩ", "Điều dưỡng", "Hộ lý",
                "Lễ tân", "Thu ngân", "Kỹ thuật viên xét nghiệm", "Dược sĩ", "Nhân viên vật tư"
        };
        String[] posDescs = {
                "Quản trị hệ thống và điều hành chung",
                "Trưởng khoa quản lý và điều hành khoa",
                "Bác sĩ khám và điều trị bệnh nhân",
                "Điều dưỡng chăm sóc bệnh nhân",
                "Hộ lý hỗ trợ chăm sóc bệnh nhân",
                "Lễ tân tiếp đón và hướng dẫn bệnh nhân",
                "Thu ngân thu tiền viện phí",
                "Kỹ thuật viên thực hiện xét nghiệm",
                "Dược sĩ cấp phát và tư vấn thuốc",
                "Nhân viên quản lý vật tư y tế"
        };

        // ✅ Tạo positions
        for (int i = 0; i < posNames.length; i++) {
            final String currentPosName = posNames[i];
            final String currentPosDesc = posDescs[i];

            try {
                List<Position> existingPositions = positionRepository.findAll();
                Position existingPos = existingPositions.stream()
                        .filter(pos -> currentPosName.equals(pos.getName()))
                        .findFirst()
                        .orElse(null);

                if (existingPos == null) {
                    Position pos = new Position();
                    pos.setName(currentPosName);
                    pos.setDescription(currentPosDesc);
                    positionRepository.save(pos);
                    System.out.println("   ✅ Created position: " + currentPosName);
                }
            } catch (Exception e) {
                System.err.println("   ❌ Error creating position " + currentPosName + ": " + e.getMessage());
            }
        }
    }

    private void createDefaultAdminAccount() {
        System.out.println("👨‍💼 Checking admin account...");

        try {
            // Check xem đã có admin chưa
            Employee existingAdmin = employeeRepository.findByEmail("admin@hospital.com");
            if (existingAdmin != null) {
                System.out.println("   ℹ️ Admin account already exists: admin@hospital.com");
                return;
            }

            // ✅ Tìm ADMIN role
            List<Role> allRoles = roleRepository.findAll();
            Role adminRole = allRoles.stream()
                    .filter(role -> "ADMIN".equals(role.getCode()))
                    .findFirst()
                    .orElse(null);

            if (adminRole == null) {
                System.err.println("   ❌ ADMIN role not found! Cannot create admin account.");
                return;
            }

            // ✅ Tìm Department và Position
            List<Department> allDepts = departmentRepository.findAll();
            Department adminDept = allDepts.stream()
                    .filter(dept -> "Phòng Hành chính".equals(dept.getName()))
                    .findFirst()
                    .orElse(null);

            List<Position> allPositions = positionRepository.findAll();
            Position adminPos = allPositions.stream()
                    .filter(pos -> "Quản trị viên".equals(pos.getName()))
                    .findFirst()
                    .orElse(null);

            if (adminDept == null || adminPos == null) {
                System.err.println("   ❌ Admin department or position not found!");
                return;
            }

            // Tạo Employee Admin
            Employee admin = new Employee();
            admin.setName("System Administrator");
            admin.setPhone("0123456789");
            admin.setEmail("admin@hospital.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setGender(true);
            admin.setAddress("Hệ thống tự động tạo");
            admin.setStartingDate(LocalDate.now());
            admin.setStatus(true);
            admin.setAvatar(null);
            admin.setDepartment(adminDept);
            admin.setPosition(adminPos);

            // Save employee
            Employee savedAdmin = employeeRepository.save(admin);
            System.out.println("   ✅ Created admin employee: " + savedAdmin.getEmail());

            // Assign ADMIN role
            EmployeeRole employeeRole = new EmployeeRole();
            employeeRole.setEmployee(savedAdmin);
            employeeRole.setRole(adminRole);
            employeeRoleRepository.save(employeeRole);

            System.out.println("   ✅ Assigned ADMIN role to: " + savedAdmin.getEmail());
            System.out.println("");
            System.out.println("🎉 DEFAULT ADMIN ACCOUNT CREATED SUCCESSFULLY!");
            System.out.println("📧 Email: admin@hospital.com");
            System.out.println("🔑 Password: admin123");
            System.out.println("⚠️ Please change the default password after first login!");
            System.out.println("");

        } catch (Exception e) {
            System.err.println("   ❌ Error creating admin account: " + e.getMessage());
            e.printStackTrace();
        }
    }
}