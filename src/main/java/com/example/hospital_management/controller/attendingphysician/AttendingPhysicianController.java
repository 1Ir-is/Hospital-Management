package com.example.hospital_management.controller.attendingphysician;


import com.example.hospital_management.dto.InpatientTreatmentDto;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.InpatientTreatment;
import com.example.hospital_management.entity.Medicine;
import com.example.hospital_management.service.IImpatientRecordService;
import com.example.hospital_management.service.IInpatientTreatmentService;
import com.example.hospital_management.service.IMedicineService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/attending-physician")
public class AttendingPhysicianController {
    public final IImpatientRecordService impatientRecordService;
    public final IInpatientTreatmentService iInpatientTreatmentService;
    public final IMedicineService iMedicineService;

    public AttendingPhysicianController(IImpatientRecordService impatientRecordService, IInpatientTreatmentService iInpatientTreatmentService, IMedicineService iMedicineService) {
        this.impatientRecordService = impatientRecordService;
        this.iInpatientTreatmentService = iInpatientTreatmentService;
        this.iMedicineService = iMedicineService;
    }

    @ModelAttribute("medicineList")
    public List<Medicine> getListMedicine(){
        return iMedicineService.getAll();
    }

    @ModelAttribute("locationList")
    public List<String> getListLocation(){
        List<String> locationList = new ArrayList<>();
        locationList.add("Bệnh viện");
        locationList.add("Tại nhà");
        return locationList;
    }

    @GetMapping("")
    public String getAllImpatientRecord(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String patientName,
            Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<ImpatientRecord> impatientRecords = impatientRecordService.findAll(patientName, pageable);
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("patientName", patientName);
        model.addAttribute("size", size);
        return "attending_physician/list";
    }

    @GetMapping("/{id}/create")
    public String setInpatientTreatment( @PathVariable Long id, Model model){
        ImpatientRecord impatientRecord = impatientRecordService.getImpatientRecordById(id);
        InpatientTreatmentDto inpatientTreatmentDto = new InpatientTreatmentDto();
        inpatientTreatmentDto.setImpatientRecord(impatientRecord);
        model.addAttribute(inpatientTreatmentDto);
        return "attending_physician/create";
    }

    @PostMapping("/{id}/create")
    public String postInpatientTreatment(
            @Validated @ModelAttribute("inpatientTreatmentDto")InpatientTreatmentDto inpatientTreatmentDto, BindingResult bindingResult,
            @PathVariable Long id){
        InpatientTreatment inpatientTreatment = new InpatientTreatment();
        new InpatientTreatmentDto().validate(inpatientTreatmentDto,bindingResult);
        if(bindingResult.hasErrors()){
            return "redirect:/attending-physician/" + id + "/create";
        }
        BeanUtils.copyProperties(inpatientTreatmentDto,inpatientTreatment);
        iInpatientTreatmentService.save(inpatientTreatment);
        return "attending_physician/list";
    }
}
