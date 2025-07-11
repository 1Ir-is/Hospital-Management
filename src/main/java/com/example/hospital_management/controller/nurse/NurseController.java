package com.example.hospital_management.controller.nurse;

import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.VitalSign;
import com.example.hospital_management.service.IMedicalRecordService;
import com.example.hospital_management.service.IVitalSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/nurse")
public class NurseController {
    private final IMedicalRecordService medicalRecordService;
    private final IVitalSignService vitalSignService;

    @Autowired
    public NurseController(IMedicalRecordService medicalRecordService, IVitalSignService vitalSignService) {
        this.medicalRecordService = medicalRecordService;
        this.vitalSignService = vitalSignService;
    }

    @GetMapping("vital-signs/check-non")
    public String showListNeedMeasure(@RequestParam(required = false, defaultValue = "0") int page,
                                      @RequestParam(required = false, defaultValue = "5") int size,
                                      Model model) {
        Pageable pageable = PageRequest.of(page,size);
        Page<MedicalRecord> medicalRecordList = medicalRecordService.findAllWithOutVitalSign(pageable);
        model.addAttribute("medicalRecordList",medicalRecordList.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", medicalRecordList.getTotalPages());
        return "nurse/list";
    }

    @GetMapping("vital-signs/{id}/measure")
    public String showMeasureForm(@PathVariable Long id,Model model) {
        model.addAttribute("medicalRecord",medicalRecordService.findById(id));
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
        redirectAttributes.addFlashAttribute("success","Cập nhật thông số sinh học cho bệnh nhân thành công");
        return "redirect:/nurse/vital-signs/check-non";
    }
}
