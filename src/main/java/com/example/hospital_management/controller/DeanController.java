package com.example.hospital_management.controller;

import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
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

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class DeanController {
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IImpatientRecordService impatientRecordService;
    @Autowired
    private IEmployeeAssignmentService employeeAssignmentService;
    @Autowired
    private IClinicalExaminationService clinicalExaminationService;
    @Autowired
    private ISurgeryService surgeryService;
    @Autowired
    private ISurgeryTypeService surgeryTypeService;
    @Autowired
    private IDepartmentService departmentService;


    @GetMapping("/department-head")
    public String viewEmployeeByDepartment(HttpSession session, Model model) {
        Long departmentId = (Long) session.getAttribute("departmentId");
        if (departmentId == null) {
            throw new RuntimeException("Không tìm thấy khoa.");
        }

        List<Employee> employeeList = employeeService.findByDepartment(departmentId);
        model.addAttribute("employeeList", employeeList);
        return "dean/employeeByDepartment";
    }

    @GetMapping("/department-head/dashboard")
    public String showDashboard(HttpSession session, Model model, Principal principal) {
        String email = principal.getName();
        Employee current = employeeService.findByEmail(email);
        if (current == null || current.getDepartment() == null) {
            throw new RuntimeException("Không tìm thấy khoa. Vui lòng chọn lại.");
        }

        Long departmentId = current.getDepartment().getId();
        session.setAttribute("departmentId", departmentId);
        String departmentName = current.getDepartment().getName();
        session.setAttribute("departmentName", departmentName);
        model.addAttribute("departmentId", departmentId);

        return "dean/dashboard"; // ✅ TRẢ VỀ TRANG DÙNG LAYOUT MỚI
    }





    @GetMapping("/department-head/list-inpatient")
    public String listInpatients(
            @RequestParam(defaultValue = "") String patientName,
            @RequestParam(defaultValue = "") String roomNumber,
            @RequestParam(defaultValue = "") String doctorName,
            @RequestParam(defaultValue = "") String nurseName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpSession session,
            Model model) {

        Long departmentId = (Long) session.getAttribute("departmentId");
        if (departmentId == null) {
            throw new RuntimeException("Không xác định được khoa.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("admissionDate").descending());
        Page<ImpatientRecord> recordPage = impatientRecordService.searchByFieldsWithDepartment(
                patientName, roomNumber, doctorName, nurseName, departmentId, pageable
        );

        recordPage.getContent().forEach(record -> {
            employeeAssignmentService.findDoctorByRecordId(record.getId())
                    .ifPresent(record::setAssignedDoctor);
            employeeAssignmentService.findNurseByRecordId(record.getId())
                    .ifPresent(record::setAssignedNurse);
        });

        model.addAttribute("departmentId", departmentId);
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
        employeeAssignmentService.assignDoctorToRecord(recordId, employeeId);
        employeeAssignmentService.assignNurseToRecord(recordId, nurseId);
        redirect.addFlashAttribute("success", "Phân công thành công!");
        return "redirect:/department-head/list-inpatient?departmentId=" + departmentId;
    }



    @GetMapping("/department-head/inpatient/detail")
    public String viewDetail(@RequestParam Long recordId, Model model) {
        ImpatientRecord record = impatientRecordService.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nội trú"));

        List<ClinicalExamination> history = clinicalExaminationService.getByImpatientRecordId(recordId);

        model.addAttribute("record", record);
        model.addAttribute("historyList", history); // 👈 danh sách khám bệnh

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
    @GetMapping("/department-head/surgeries/assign-form")
    public String showForm(@RequestParam Long recordId,
                           @RequestParam Long departmentId,
                           Model model) {
        ImpatientRecord record = impatientRecordService.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ"));
        List<Employee> doctorList = employeeService.findDoctorsByDepartment(departmentId);
        List<SurgeryType> surgeryTypeList = surgeryTypeService.findAll();

        Optional<Surgery> surgeryOpt = surgeryService.findByRecordId(recordId);

        model.addAttribute("record", record);
        model.addAttribute("departmentId", departmentId);
        model.addAttribute("doctorList", doctorList);
        model.addAttribute("surgeryTypeList", surgeryTypeList);
        model.addAttribute("surgery", surgeryOpt.orElse(new Surgery()));

        return "dean/phan_cong_phau_thuat";
    }

    @PostMapping("/department-head/surgeries/save")
    public String saveSurgery(@RequestParam Long recordId,
                              @RequestParam Long employeeId,
                              @RequestParam Long surgeryTypeId,
                              @RequestParam String date,
                              @RequestParam(required = false) String note,
                              RedirectAttributes redirect) {

        Surgery surgery = surgeryService.findByRecordId(recordId).orElse(new Surgery());
        surgery.setImpatientRecord(impatientRecordService.findById(recordId).orElse(null));
        surgery.setEmployee(employeeService.findById(employeeId).orElse(null));
        surgery.setSurgeryType(surgeryTypeService.findById(surgeryTypeId).orElse(null));
        surgery.setDate(LocalDate.parse(date));
        surgery.setNote(note);

        surgeryService.save(surgery);

        redirect.addFlashAttribute("success", "Phân công phẫu thuật thành công!");
        return "redirect:/department-head/list-inpatient";
    }







}
