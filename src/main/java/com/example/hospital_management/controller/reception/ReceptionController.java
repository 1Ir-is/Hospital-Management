package com.example.hospital_management.controller.reception;


import com.example.hospital_management.dto.ImpatientRecordDto;
import com.example.hospital_management.entity.Bed;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.Room;
import com.example.hospital_management.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/receptionist")
public class ReceptionController {

    private final IBedService iBedService;
    private final IRoomService iRoomService;
    private final IMedicalRecordService iMedicalRecordService;
    private final IImpatientRecordService impatientRecordService;

    public ReceptionController(IBedService iBedService, IRoomService iRoomService, IMedicalRecordService iMedicalRecordService, IImpatientRecordService impatientRecordService) {
        this.iBedService = iBedService;
        this.iRoomService = iRoomService;
        this.iMedicalRecordService = iMedicalRecordService;
        this.impatientRecordService = impatientRecordService;
    }

    @ModelAttribute("bedList")
    public List<Bed> bedList() {
        return iBedService.getListBedNotUsage();
    }

    @ModelAttribute("sizeList")
    public List<Integer> sizeList() {
        return Arrays.asList(5, 10, 15, 20, 25);
    }

    @ModelAttribute("roomList")
    public List<Room> roomList() {
        return iRoomService.findAll();
    }

    @GetMapping("")
    public String getListWaitingToImpatient(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String patientName,
            @RequestParam(required = false, defaultValue = "") String code,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImpatientRecord> impatientRecords = impatientRecordService.findAllWaitingToImpatient(patientName,code, pageable);
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("patientName", patientName);
        model.addAttribute("size", size);
        model.addAttribute("code", code);
        return "reception/list-admission";
    }

    @GetMapping("/admissions/{id}/create")
    public String getFormAdmissions(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("impatientRecordDto")) {
            ImpatientRecord impatientRecord = impatientRecordService.getImpatientRecordById(id);
            ImpatientRecordDto dto = new ImpatientRecordDto();
            dto.setMedicalRecord(impatientRecord.getMedicalRecord());
            dto.setReason(impatientRecord.getReason());
            model.addAttribute("impatientRecordDto", dto);
        } else {
            ImpatientRecordDto dto = (ImpatientRecordDto) model.getAttribute("impatientRecordDto");

            if (dto.getMedicalRecord() != null) {
                if (dto.getMedicalRecord().getExaminationShift() == null) {
                    Long medicalRecordId = dto.getMedicalRecord().getId();
                    if (medicalRecordId != null) {
                        dto.setMedicalRecord(iMedicalRecordService.getMedicalRecordById(medicalRecordId));
                    }
                }
            }
        }
        return "/reception/create-admission";
    }


    @GetMapping("/admission/getBed")
    @ResponseBody
    public List<Map<String, Object>> getBedsByRoomId(@RequestParam("roomId") Integer roomId) {
        List<Bed> beds = iBedService.findAvailableBedsByRoomId(roomId);
        return beds.stream().map(b -> {
            Map<String, Object> bedMap = new HashMap<>();
            bedMap.put("id", b.getId());
            bedMap.put("number", b.getNumber());
            return bedMap;
        }).collect(Collectors.toList());
    }

    @PostMapping("/admissions/{id}/create")
    public String postAdmissions(@Validated @ModelAttribute("impatientRecordDto") ImpatientRecordDto impatientRecordDto,@PathVariable Long id, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ImpatientRecord impatientRecord = new ImpatientRecord();

        new ImpatientRecordDto().validate(impatientRecordDto, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("impatientRecordDto", impatientRecordDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.impatientRecordDto", bindingResult);
            return "redirect:/receptionist/admissions/" + id + "/create";
        }
        BeanUtils.copyProperties(impatientRecordDto, impatientRecord);
        impatientRecord.setStatus(true);
        impatientRecord.setId(id);
        impatientRecordService.save(impatientRecord);
        return "redirect:/receptionist";
    }


}
