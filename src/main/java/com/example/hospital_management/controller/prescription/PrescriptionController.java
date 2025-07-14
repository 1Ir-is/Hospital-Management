package com.example.hospital_management.controller.prescription;

import com.example.hospital_management.dto.PrescriptionDetailDto;
import com.example.hospital_management.dto.PrescriptionDto;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/prescription")
@SessionAttributes("prescription")
public class PrescriptionController {
    private final IMedicalRecordService medicalRecordService;
    private final IMedicineService medicineService;
    private final IPrescriptionService prescriptionService;
    private final IPrescriptionDetailService prescriptionDetailService;
    private final IEmployeeService employeeService;
    private final IExaminationShiftService examinationShiftService;
    private final IExaminationShiftStatusService examinationShiftStatusService;

    public PrescriptionController(IMedicalRecordService medicalRecordService, IMedicineService medicineService, IPrescriptionService prescriptionService, IPrescriptionDetailService prescriptionDetailService, IEmployeeService employeeService, IExaminationShiftService examinationShiftService, IExaminationShiftStatusService examinationShiftStatusService) {
        this.medicalRecordService = medicalRecordService;
        this.medicineService = medicineService;
        this.prescriptionService = prescriptionService;
        this.prescriptionDetailService = prescriptionDetailService;
        this.employeeService = employeeService;
        this.examinationShiftService = examinationShiftService;
        this.examinationShiftStatusService = examinationShiftStatusService;
    }

    @ModelAttribute("prescription")
    public PrescriptionDto getPrescription(){
        return new PrescriptionDto();
    }

    @GetMapping("/{medicalRecordId}")
    public String chooseMedicine(@PathVariable("medicalRecordId") Long id,
                                 @ModelAttribute("prescription") PrescriptionDto prescriptionDto,
                                 Model model){
        MedicalRecord medicalRecord = medicalRecordService.findById(id);
        prescriptionDto.setMedicalRecord(medicalRecord);
        prescriptionDto.setCreatedDate(LocalDate.now());

        if(prescriptionDto.getPrescriptionDetails() == null){
            prescriptionDto.setPrescriptionDetails(new ArrayList<>());
        }

        model.addAttribute("medicalRecord", medicalRecord);
        model.addAttribute("prescriptionList", prescriptionDto.getPrescriptionDetails());
        return "/prescription/list";
    }

    @PostMapping("/{medicalRecordId}")
    public String saveNote(@PathVariable("medicalRecordId") Long id,
                                 @RequestParam(required = false) String note,
                                 @RequestParam(required = false) String conclusion,
                                 Model model){
        MedicalRecord medicalRecord = medicalRecordService.findById(id);

        if(note != null){
            medicalRecord.setNote(note);
        } else if (conclusion != null) {
            medicalRecord.setConclusion(conclusion);
        }

        medicalRecordService.save(medicalRecord);
        return "redirect:/prescription/" + id;
    }

    @GetMapping("/form")
    public String form(Model model,
                       @ModelAttribute("prescription") PrescriptionDto prescriptionDto){
        List<Medicine> medicineList = medicineService.findAll();

        if(prescriptionDto.getPrescriptionDetails() == null){
            prescriptionDto.setPrescriptionDetails(new ArrayList<>());
        }
        Long medicalRecordId = prescriptionDto.getMedicalRecord().getId();
        List<Medicine> availableMedicine = medicineList.stream()
                        .filter(medicine -> prescriptionDto.getPrescriptionDetails().stream()
                                .noneMatch(detail -> detail.getMedicine().getId().equals(medicine.getId())))
                                .collect(Collectors.toList());

        model.addAttribute("medicineList", availableMedicine);
//        model.addAttribute("medicalRecordId", medicalRecordId);
        return "/prescription/form";
    }

    @GetMapping("/{medicalRecordId}/delete/{medicineId}")
    public String delete(@PathVariable Long medicineId,
                         @PathVariable Long medicalRecordId,
                         @ModelAttribute("prescription") PrescriptionDto prescriptionDto){
        for (int i = 0; i < prescriptionDto.getPrescriptionDetails().size(); i++) {
            if(prescriptionDto.getPrescriptionDetails().get(i).getMedicine().getId().equals(medicineId)){
                prescriptionDto.getPrescriptionDetails().remove(i);
                break;
            }
        }

        return "redirect:/prescription/" + medicalRecordId;
    }


    @PostMapping("/form/add")
    public String add(RedirectAttributes attributes,
                      @RequestParam("medicine") Long medicineId,
                      @RequestParam("duration") Integer duration,
                      @RequestParam("quantity") Integer quantity,
                      @RequestParam("instruction") String instruction,
                      @ModelAttribute("prescription") PrescriptionDto prescriptionDto
                      ){

        if (prescriptionDto.getPrescriptionDetails() == null) {
            prescriptionDto.setPrescriptionDetails(new ArrayList<>());
        }

        Medicine medicine = medicineService.findById(medicineId).get();
        PrescriptionDetailDto prescriptionDetailDto =
                new PrescriptionDetailDto(null, instruction, duration, quantity, medicine);

        prescriptionDto.getPrescriptionDetails().add(prescriptionDetailDto);
        return "redirect:/prescription/" + prescriptionDto.getMedicalRecord().getId();
    }

    @GetMapping("/confirm")
    public String confirm(@ModelAttribute("prescription") PrescriptionDto prescriptionDto,
                          SessionStatus sessionStatus){
        Prescription prescription = new Prescription();
        BeanUtils.copyProperties(prescriptionDto, prescription);
        prescription.setStatus(false);
        Prescription savedPrescription = prescriptionService.save(prescription);
        for (PrescriptionDetailDto dto : prescriptionDto.getPrescriptionDetails()){
            PrescriptionDetail detail = new PrescriptionDetail();
            BeanUtils.copyProperties(dto, detail);
            detail.setPrescription(savedPrescription);
            prescriptionDetailService.save(detail);
        }
        Long medicalRecordId = savedPrescription.getMedicalRecord().getId();
        ExaminationShift shift = examinationShiftService.findByMedicalRecordId(medicalRecordId);
        shift.setExaminationShiftStatus(examinationShiftStatusService.findById(5L));
        examinationShiftService.save(shift);
        sessionStatus.setComplete();
        return "redirect:/examination";
    }

//    @PostMapping("{id}/confirm")
//    public String confirm(@ModelAttribute("prescription") PrescriptionDto prescriptionDto,
//                          @RequestParam("conclusion") String conclusion,
//                          @PathVariable("id") Long medicalRecordId,
//                          SessionStatus sessionStatus){
//        Employee employee = employeeService.findById(1L).get();
//        MedicalRecord medicalRecord = medicalRecordService.findById(medicalRecordId);
//        Prescription prescription = new Prescription();
//
//        prescriptionDto.setMedicalRecord(medicalRecord);
//        prescriptionDto.setId(null);
//        BeanUtils.copyProperties(prescriptionDto, prescription);
//        Prescription savedPrescription = prescriptionService.save(prescription);
//        for (PrescriptionDetailDto dto : prescriptionDto.getPrescriptionDetails()){
//            PrescriptionDetail detail = new PrescriptionDetail();
//            BeanUtils.copyProperties(dto, detail);
//            detail.setPrescription(savedPrescription);
//            prescriptionDetailService.save(detail);
//        }
//
//
//
//
//        medicalRecord.setStatus(true);
//        medicalRecord.setConclusion(conclusion);
//        medicalRecordService.save(medicalRecord);
//
//        ExaminationShift shift = examinationShiftService.findByMedicalRecord(medicalRecord);
//        shift.setEmployee(employee);
//        examinationShiftService.save(shift);
//
//        sessionStatus.setComplete();
//
//        return "redirect:/examination";
//    }

}
