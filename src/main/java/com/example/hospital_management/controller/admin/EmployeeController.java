package com.example.hospital_management.controller.admin;

import com.example.hospital_management.dto.EmployeeFormDTO;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import com.example.hospital_management.util.CloudinaryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IEmployeeRoleRService employeeRoleService;

    @Autowired
    private CloudinaryUtil cloudinaryService;

    @GetMapping()
    public String showListEmployees(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String status, Model model) {
        // ✅ Set activeMenu ngay từ đầu
        model.addAttribute("activeMenu", "employees");

        try {
            // Create Pageable object
            Pageable pageable = PageRequest.of(page, size);

            // Get paginated and filtered employees
            Page<Employee> employeePage = employeeService.findEmployeesWithFilters(search, status, pageable);

            // Add paginated data to model
            model.addAttribute("employees", employeePage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", employeePage.getTotalPages());
            model.addAttribute("totalElements", employeePage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("search", search);
            model.addAttribute("status", status);

            // Calculate TOTAL statistics
            long totalEmployeesCount = employeeService.countTotalEmployees();
            long activeCount = employeeService.countActiveEmployees();
            long inactiveCount = employeeService.countInactiveEmployees();
            long newEmployeesCount = employeeService.countNewEmployeesThisMonth();

            model.addAttribute("totalEmployeesCount", totalEmployeesCount);
            model.addAttribute("activeEmployeesCount", activeCount);
            model.addAttribute("inactiveEmployeesCount", inactiveCount);
            model.addAttribute("newEmployeesCount", newEmployeesCount);

            if (!search.isEmpty() || !status.isEmpty()) {
                model.addAttribute("filteredCount", employeePage.getTotalElements());
                model.addAttribute("isFiltered", true);
                model.addAttribute("filterInfo", createFilterInfo(search, status));
            } else {
                model.addAttribute("isFiltered", false);
            }


            return "admin/employee/list";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi tải danh sách nhân viên");
            return "admin/employee/list";
        }
    }

    private String createFilterInfo(String search, String status) {
        StringBuilder info = new StringBuilder();
        if (!search.isEmpty()) {
            info.append("Tìm kiếm: \"").append(search).append("\"");
        }
        if (!status.isEmpty()) {
            if (info.length() > 0) info.append(", ");
            info.append("Trạng thái: ");
            info.append(status.equals("active") ? "Hoạt động" : "Ngừng hoạt động");
        }
        return info.toString();
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // ✅ Set activeMenu đúng
        model.addAttribute("activeMenu", "employees");

        EmployeeFormDTO employeeForm = new EmployeeFormDTO();
        model.addAttribute("employeeForm", employeeForm);

        List<Department> departments = departmentService.findAllDepartmentsForDropdown();
        List<Position> positions = positionService.findAllPositions();
        List<Role> roles = roleService.findAllRoles();

        model.addAttribute("departments", departments);
        model.addAttribute("positions", positions);
        model.addAttribute("roles", roles);


        return "admin/employee/create-form";
    }

    @PostMapping("/create")
    public String createEmployee(@ModelAttribute("employeeForm") EmployeeFormDTO employeeForm, @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== RECEIVED FORM DATA ===");
            System.out.println("Name: " + employeeForm.getName());
            System.out.println("Email: " + employeeForm.getEmail());
            System.out.println("Avatar URL: " + employeeForm.getAvatar());
            System.out.println("Avatar File: " + (avatarFile != null ? avatarFile.getOriginalFilename() : "null"));

            Employee employee = new Employee();
            employee.setName(employeeForm.getName());
            employee.setPhone(employeeForm.getPhone());
            employee.setEmail(employeeForm.getEmail());
            employee.setPassword(employeeForm.getPassword());
            employee.setGender(employeeForm.getGender());
            employee.setAddress(employeeForm.getAddress());
            employee.setStartingDate(employeeForm.getStartingDate());
            employee.setStatus(employeeForm.getStatus());

            // Handle avatar upload
            String avatarUrl = null;
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    // Upload file to Cloudinary
                    avatarUrl = cloudinaryService.uploadFile(avatarFile);
                    System.out.println("Uploaded avatar URL: " + avatarUrl);
                } catch (Exception e) {
                    System.err.println("Error uploading avatar: " + e.getMessage());
                    redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi upload ảnh: " + e.getMessage());
                    return "redirect:/admin/employees/create";
                }
            } else if (employeeForm.getAvatar() != null && !employeeForm.getAvatar().isEmpty()) {
                // Use URL if provided
                avatarUrl = employeeForm.getAvatar();
                System.out.println("Using avatar URL: " + avatarUrl);
            }
            employee.setAvatar(avatarUrl);

            if (employeeForm.getDepartmentId() != null) {
                Department department = departmentService.findDepartmentById(employeeForm.getDepartmentId());
                employee.setDepartment(department);
            }

            if (employeeForm.getPositionId() != null) {
                Position position = positionService.findPositionById(employeeForm.getPositionId());
                employee.setPosition(position);
            }

            employeeService.saveEmployeeWithRoles(employee, employeeForm.getRoleIds());

            redirectAttributes.addFlashAttribute("successMessage", "Nhân viên " + employee.getName() + " đã được tạo thành công!");

            return "redirect:/admin/employees";
        } catch (Exception e) {
            System.err.println("Error creating employee: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi tạo nhân viên: " + e.getMessage());
            return "redirect:/admin/employees/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("activeMenu", "employees");
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            return "redirect:/admin/employees";
        }

        EmployeeFormDTO employeeForm = new EmployeeFormDTO();
        employeeForm.setId(employee.getId());
        employeeForm.setName(employee.getName());
        employeeForm.setPhone(employee.getPhone());
        employeeForm.setEmail(employee.getEmail());
        employeeForm.setPassword(employee.getPassword());
        employeeForm.setGender(employee.getGender());
        employeeForm.setAddress(employee.getAddress());
        employeeForm.setStartingDate(employee.getStartingDate());
        employeeForm.setStatus(employee.getStatus());
        employeeForm.setAvatar(employee.getAvatar());

        if (employee.getDepartment() != null) {
            employeeForm.setDepartmentId(employee.getDepartment().getId());
        }
        if (employee.getPosition() != null) {
            employeeForm.setPositionId(employee.getPosition().getId());
        }

        List<EmployeeRole> employeeRoles = employeeRoleService.findByEmployeeId(id);
        List<Long> currentRoleIds = employeeRoles.stream().map(er -> er.getRole().getId()).collect(Collectors.toList());
        employeeForm.setRoleIds(currentRoleIds);

        model.addAttribute("employeeForm", employeeForm);
        model.addAttribute("departments", departmentService.findAllDepartmentsForDropdown());
        model.addAttribute("positions", positionService.findAllPositions());
        model.addAttribute("roles", roleService.findAllRoles());

        return "admin/employee/edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id, @ModelAttribute("employeeForm") EmployeeFormDTO employeeForm, @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile, RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeService.findEmployeeById(id);
            if (employee == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy nhân viên!");
                return "redirect:/admin/employees";
            }

            employee.setName(employeeForm.getName());
            employee.setPhone(employeeForm.getPhone());
            employee.setEmail(employeeForm.getEmail());
            employee.setPassword(employeeForm.getPassword());
            employee.setGender(employeeForm.getGender());
            employee.setAddress(employeeForm.getAddress());
            employee.setStartingDate(employeeForm.getStartingDate());
            employee.setStatus(employeeForm.getStatus());

            // Handle avatar upload
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    // Delete old avatar if exists
                    if (employee.getAvatar() != null && !employee.getAvatar().isEmpty()) {
                        String oldPublicId = cloudinaryService.extractPublicId(employee.getAvatar());
                        if (oldPublicId != null) {
                            cloudinaryService.deleteFile(oldPublicId);
                        }
                    }

                    // Upload new avatar
                    String newAvatarUrl = cloudinaryService.uploadFile(avatarFile);
                    employee.setAvatar(newAvatarUrl);
                } catch (Exception e) {
                    System.err.println("Error uploading avatar: " + e.getMessage());
                    redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi upload ảnh: " + e.getMessage());
                    return "redirect:/admin/employees/edit/" + id;
                }
            } else if (employeeForm.getAvatar() != null && !employeeForm.getAvatar().isEmpty()) {
                // Use URL if provided
                employee.setAvatar(employeeForm.getAvatar());
            }

            if (employeeForm.getDepartmentId() != null) {
                Department department = departmentService.findDepartmentById(employeeForm.getDepartmentId());
                employee.setDepartment(department);
            }

            if (employeeForm.getPositionId() != null) {
                Position position = positionService.findPositionById(employeeForm.getPositionId());
                employee.setPosition(position);
            }

            employeeService.saveEmployee(employee);
            employeeService.updateEmployeeRoles(id, employeeForm.getRoleIds());

            redirectAttributes.addFlashAttribute("successMessage", "Nhân viên " + employee.getName() + " đã được cập nhật thành công!");

            return "redirect:/admin/employees";
        } catch (Exception e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật nhân viên: " + e.getMessage());
            return "redirect:/admin/employees/edit/" + id;
        }
    }


    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeService.findEmployeeById(id);
            if (employee != null) {
                String employeeName = employee.getName();
                employeeService.deleteEmployeeById(id);
                redirectAttributes.addFlashAttribute("successMessage", "Nhân viên " + employeeName + " đã được xóa thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy nhân viên!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa nhân viên: " + e.getMessage());
        }
        return "redirect:/admin/employees";
    }
}