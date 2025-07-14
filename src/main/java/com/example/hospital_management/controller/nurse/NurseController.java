package com.example.hospital_management.controller.nurse;

import com.example.hospital_management.dto.AdvancePaymentDto;
import com.example.hospital_management.dto.InpatientTreatmentDto;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import com.example.hospital_management.service.impl.ImpatientRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/nurse")
@PreAuthorize("hasAnyRole('NURSE', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class NurseController {
    private final IMedicalRecordService medicalRecordService;
    private final IVitalSignService vitalSignService;
    private final ImpatientRecordService impatientRecordService;
    private final IAdvancePaymentService iAdvancePaymentService;
    private final IEmployeeService iEmployeeService;
    public final IInpatientTreatmentService iInpatientTreatmentService;
    public final IEmployeeAssignmentService employeeAssignmentService;

    public NurseController(ImpatientRecordService impatientRecordService,
                           IAdvancePaymentService iAdvancePaymentService,
                           IEmployeeService iEmployeeService,
                           IMedicalRecordService medicalRecordService,
                           IVitalSignService vitalSignService, IInpatientTreatmentService iInpatientTreatmentService, IEmployeeAssignmentService employeeAssignmentService) {
        this.impatientRecordService = impatientRecordService;
        this.iAdvancePaymentService = iAdvancePaymentService;
        this.iEmployeeService = iEmployeeService;
        this.medicalRecordService = medicalRecordService;
        this.vitalSignService = vitalSignService;
        this.iInpatientTreatmentService = iInpatientTreatmentService;
        this.employeeAssignmentService = employeeAssignmentService;
    }

    @ModelAttribute("sizeList")
    public List<Integer> sizeList() {
        return Arrays.asList(5, 10, 15, 20, 25);
    }

    @ModelAttribute("impatientRecordList")
    public List<ImpatientRecord> impatientRecords() {
        return impatientRecordService.findAll();
    }

//    @GetMapping()
//    public String nurseDashboard(Model model) {
//        model.addAttribute("pageTitle", "Điều dưỡng");
//        model.addAttribute("userRole", "Điều dưỡng");
//        return "nurse/index";
//    }

    @GetMapping("vital-signs/check-non")
    public String showListNeedMeasure(@RequestParam(required = false, defaultValue = "0") int page,
                                      @RequestParam(required = false, defaultValue = "5") int size,
                                      Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MedicalRecord> medicalRecordList = medicalRecordService.findAllWithOutVitalSign(pageable);
        model.addAttribute("medicalRecordList", medicalRecordList.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", medicalRecordList.getTotalPages());
        return "nurse/list";
    }

    @GetMapping("")
    public String findAll(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String patientName,
            @RequestParam(required = false, defaultValue = "4") Long employeeId,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImpatientRecord> impatientRecords = impatientRecordService.findAll(patientName, employeeId, pageable);
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("patientName", patientName);
        model.addAttribute("size", size);
        return "nurse/patient/list";
    }

    @GetMapping("/list-impatient-records")
    public String findAllImpatientRecords(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String patientName,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImpatientRecord> impatientRecords = impatientRecordService.findAll(patientName, pageable);
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("patientName", patientName);
        model.addAttribute("size", size);
        return "nurse/patient/list-all";
    }

    @GetMapping("/advance-payments")
    public String findAllAdvancePayment(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String patientName,
            @RequestParam(required = false, defaultValue = "1") Long employeeId,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AdvancePayment> advancePayments = iAdvancePaymentService.findAll(patientName, employeeId, pageable);
        model.addAttribute("advancePayments", advancePayments);
        model.addAttribute("patientName", patientName);
        model.addAttribute("size", size);
        return "nurse/fee/list";
    }

    @GetMapping("vital-signs/{id}/measure")
    public String showMeasureForm(@PathVariable Long id, Model model) {
        model.addAttribute("medicalRecord", medicalRecordService.findById(id));
        model.addAttribute("vitalSign", new VitalSign());
        return "nurse/measure";
    }

    @PostMapping("vital-signs/check")
    public String saveVitalSign(VitalSign vitalSign, @RequestParam Long medicalRecordId,
                                RedirectAttributes redirectAttributes) {
        vitalSignService.save(vitalSign);
        MedicalRecord medicalRecord = medicalRecordService.findById(medicalRecordId);
        medicalRecord.setVitalSign(vitalSign);
        medicalRecordService.save(medicalRecord);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông số sinh học cho bệnh nhân thành công");
        return "redirect:/nurse/vital-signs/check-non";
    }


    @GetMapping("/advance-payments/create")
    public String createAdvancePayment(Model model) {
        model.addAttribute(new AdvancePaymentDto());
        return "nurse/fee/create";
    }

    @PostMapping("/advance-payments/create")
    public String createAdvancePayment(@Validated @ModelAttribute("advancePaymentDto") AdvancePaymentDto advancePaymentDto, BindingResult bindingResult, Model model) {
        AdvancePayment advancePayment = new AdvancePayment();
        new AdvancePaymentDto().validate(advancePaymentDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "nurse/fee/create";
        }
        BeanUtils.copyProperties(advancePaymentDto, advancePayment);
        advancePayment.setEmployee(iEmployeeService.findEmployeeById(1L));
        iAdvancePaymentService.save(advancePayment);
        return "redirect:/nurse/advance-payments";
    }


    @GetMapping("/{id}/update")
    public String setInpatientTreatment( @PathVariable Long id, Model model){
        if(!model.containsAttribute("inpatientTreatmentDto")){
            ImpatientRecord impatientRecord = impatientRecordService.getImpatientRecordById(id);
            InpatientTreatmentDto inpatientTreatmentDto = new InpatientTreatmentDto();
            inpatientTreatmentDto.setImpatientRecord(impatientRecord);
            model.addAttribute("inpatientTreatmentDto",inpatientTreatmentDto);}
        else{
            InpatientTreatmentDto dto = (InpatientTreatmentDto) model.getAttribute("inpatientTreatmentDto");
            assert dto != null;
            dto.setImpatientRecord(impatientRecordService.getImpatientRecordById(id));
        }
        List<InpatientTreatment> treatmentHistory = iInpatientTreatmentService.findByImpatientRecordId(id);
        List<EmployeeAssignment> employeeAssignments = employeeAssignmentService.findAll(id);
        model.addAttribute("employeeAssignments", employeeAssignments);
        model.addAttribute("treatmentHistory", treatmentHistory);
        return "nurse/treatment_task/detail";
    }


}
