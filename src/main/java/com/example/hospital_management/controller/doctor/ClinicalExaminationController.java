package com.example.hospital_management.controller.doctor;


import com.example.hospital_management.entity.ClinicalExamination;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.VitalSign;
import com.example.hospital_management.service.IClinicalExaminationService;
import com.example.hospital_management.service.IImpatientRecordService;
import com.example.hospital_management.service.IVitalSignService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("clinical_examination")
public class ClinicalExaminationController {
    private IClinicalExaminationService clinicalExaminationService;
    private IImpatientRecordService impatientRecordService;
    private IVitalSignService vitalSignService;

    public ClinicalExaminationController(IClinicalExaminationService clinicalExaminationService, IImpatientRecordService impatientRecordService, IVitalSignService vitalSignService) {
        this.clinicalExaminationService = clinicalExaminationService;
        this.impatientRecordService = impatientRecordService;
        this.vitalSignService = vitalSignService;
    }

    @GetMapping("/{id}")
    public String showDailyExamination(@PathVariable Long id, Model model) {
        List<ClinicalExamination> list = clinicalExaminationService.findByImpatientRecordId(id);
//        for (ClinicalExamination ce : list) {
//            System.out.println("Date: " + ce.getDate());
//            System.out.println("Vital: " + ce.getVitalSign());
//        }
        model.addAttribute("impatientRecord", impatientRecordService.findById(id).orElse(null));
        model.addAttribute("examinations", clinicalExaminationService.findByImpatientRecordId(id));
        model.addAttribute("vitalSigns", vitalSignService.findAll());
        model.addAttribute("clinicalExamination", new ClinicalExamination());
        return "result/clinical";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute ClinicalExamination clinicalExamination,
                       @RequestParam("impatientRecordId") Long impatientRecordId) {
        clinicalExamination.setDate(LocalDate.now());

        // Cần lưu riêng VitalSign vì nó là entity độc lập
        VitalSign vitalSign = clinicalExamination.getVitalSign();
        vitalSignService.save(vitalSign); // lưu và gán ID

        // Gán VitalSign đã lưu lại vào ClinicalExamination
        clinicalExamination.setVitalSign(vitalSign);

        ImpatientRecord record = impatientRecordService.findById(impatientRecordId).orElse(null);
        clinicalExamination.setImpatientRecord(record);

        clinicalExaminationService.save(clinicalExamination);
        return "redirect:/clinical_examination/" + impatientRecordId;
    }

}
