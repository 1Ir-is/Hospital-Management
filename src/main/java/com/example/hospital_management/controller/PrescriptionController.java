package com.example.hospital_management.controller;

import com.example.hospital_management.dto.PrescriptionDetailDto;
import com.example.hospital_management.dto.PrescriptionDto;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.Medicine;
import com.example.hospital_management.entity.Prescription;
import com.example.hospital_management.entity.PrescriptionDetail;
import com.example.hospital_management.service.IMedicalRecordService;
import com.example.hospital_management.service.IMedicineService;
import com.example.hospital_management.service.IPrescriptionDetailService;
import com.example.hospital_management.service.IPrescriptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.BeanProperty;
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

    public PrescriptionController(IMedicalRecordService medicalRecordService, IMedicineService medicineService, IPrescriptionService prescriptionService, IPrescriptionDetailService prescriptionDetailService) {
        this.medicalRecordService = medicalRecordService;
        this.medicineService = medicineService;
        this.prescriptionService = prescriptionService;
        this.prescriptionDetailService = prescriptionDetailService;
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

    @GetMapping("/form")
    public String form(Model model,
                       @ModelAttribute("prescription") PrescriptionDto prescriptionDto){
        List<Medicine> medicineList = medicineService.findAll();

        if(prescriptionDto.getPrescriptionDetails() == null){
            prescriptionDto.setPrescriptionDetails(new ArrayList<>());
        }

        List<Medicine> availableMedicine = medicineList.stream()
                        .filter(medicine -> prescriptionDto.getPrescriptionDetails().stream()
                                .noneMatch(detail -> detail.getMedicine().getId().equals(medicine.getId())))
                                .collect(Collectors.toList());

        model.addAttribute("medicineList", availableMedicine);
        return "/prescription/form";
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
    public String confirm(@ModelAttribute("prescription") PrescriptionDto prescriptionDto){
        Prescription prescription = new Prescription();
        BeanUtils.copyProperties(prescriptionDto, prescription);
        Prescription savedPrescription = prescriptionService.save(prescription);
        for (PrescriptionDetailDto dto : prescriptionDto.getPrescriptionDetails()){
            PrescriptionDetail detail = new PrescriptionDetail();
            BeanUtils.copyProperties(dto, detail);
            detail.setPrescription(savedPrescription);
            prescriptionDetailService.save(detail);
        }
        MedicalRecord medicalRecord = medicalRecordService.findById(savedPrescription.getMedicalRecord().getId());
        medicalRecord.setStatus(true);
        medicalRecordService.save(medicalRecord);
        return "redirect:/examination";
    }
}
