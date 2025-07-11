package com.example.hospital_management.controller;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.service.IEmployeeAssigmentService;
import com.example.hospital_management.service.IEmployeeService;
import com.example.hospital_management.service.IImpatientRecordService;
import com.example.hospital_management.service.IPatientService;
import com.fasterxml.jackson.annotation.JacksonInject;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DeanController {
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IImpatientRecordService impatientRecordService;
    @Autowired
    private IEmployeeAssigmentService employeeAssigmentService;


    @GetMapping("/department-head")
    public String viewEmployeeByDepartment(@RequestParam("departmentId") Long departmentId,
                                           HttpSession session,
                                           Model model) {
        session.setAttribute("departmentId", departmentId); // 👈 Lưu vào session
        List<Employee> employeeList = employeeService.findByDepartment(departmentId);
        model.addAttribute("employeeList", employeeList);
        return "dean/employeeByDepartment";
    }

    @GetMapping("/department-head/list-inpatient")
    public String listInpatients(@RequestParam(defaultValue = "") String patientName,
                                 @RequestParam(defaultValue = "") String roomNumber,
                                 @RequestParam(defaultValue = "") String doctorName,
                                 @RequestParam(defaultValue = "") String nurseName,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 HttpSession session,
                                 Model model) {
        Long departmentId = (Long) session.getAttribute("departmentId"); // 👈 Lấy từ session
        if (departmentId == null) {
            throw new RuntimeException("Không tìm thấy khoa. Vui lòng chọn lại.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("admissionDate").descending());
        Page<ImpatientRecord> recordPage = impatientRecordService.searchByFields(patientName, roomNumber, doctorName, nurseName, pageable);

        recordPage.getContent().forEach(record -> {
            employeeAssigmentService.findDoctorByRecordId(record.getId())
                    .ifPresent(record::setAssignedDoctor);
            employeeAssigmentService.findNurseByRecordId(record.getId())
                    .ifPresent(record::setAssignedNurse);
        });

        model.addAttribute("departmentId", departmentId); // 👈 Truyền lại nếu cần dùng ở view
        model.addAttribute("recordList", recordPage.getContent());
        model.addAttribute("recordPage", recordPage);
        model.addAttribute("patientName", patientName);
        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("doctorName", doctorName);
        model.addAttribute("nurseName", nurseName);
        return "dean/danh-sach-benh-nhan-noi-tru";
    }




    @GetMapping("/department-head/doctors/assign-form")
    public String formPhanCongBacSi(@RequestParam Long recordId, @RequestParam Long departmentId, Model model){
        ImpatientRecord record = impatientRecordService.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nội trú"));

        model.addAttribute("record", record);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("doctorList", employeeService.findDoctorsByDepartment(departmentId));
        model.addAttribute("nurseList", employeeService.findNursesByDepartment(departmentId));
        return "dean/phan_cong_dieu_tri";
    }

    @PostMapping("/department-head/doctors/assign")
    public String luuPhanCongBacSi(@RequestParam Long recordId,
                                   @RequestParam Long employeeId,
                                   @RequestParam Long nurseId,
                                   @RequestParam Long departmentId, // 👈 giữ lại ID
                                   RedirectAttributes redirect) {
        employeeAssigmentService.assignDoctorToRecord(recordId, employeeId);
        employeeAssigmentService.assignNurseToRecord(recordId, nurseId);
        redirect.addFlashAttribute("success", "Phân công thành công!");
        return "redirect:/department-head/list-inpatient?departmentId=" + departmentId;
    }

    @GetMapping("/department-head/inpatient/detail")
    public String viewDetail(@RequestParam Long recordId, Model model) {
        ImpatientRecord record = impatientRecordService.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nội trú"));
        model.addAttribute("record", record);
        return "dean/chi_tiet_benh_nhan";
    }

    @GetMapping("/department-head/inpatient/status-form")
    public String showStatusForm(@RequestParam Long recordId, Model model) {
        ImpatientRecord record = impatientRecordService.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nội trú"));
        model.addAttribute("record", record);
        return "dean/chinh_sua_tinh_trang";
    }

    @PostMapping("/department-head/inpatient/update-status")
    public String updateStatus(@RequestParam Long recordId,
                               @RequestParam String note,
                               @RequestParam(required = false) String redirectToDetail,
                               RedirectAttributes redirect) {
        impatientRecordService.updateNote(recordId, note); // 👈 dùng note
        redirect.addFlashAttribute("success", "Cập nhật tình trạng thành công!");

        if ("true".equals(redirectToDetail)) {
            return "redirect:/department-head/inpatient/detail?recordId=" + recordId;
        }

        return "redirect:/department-head/list-inpatient";
    }








}
