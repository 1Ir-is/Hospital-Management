package com.example.hospital_management.controller;

import com.example.hospital_management.entity.Room;
import com.example.hospital_management.service.IMedicalRecordService;
import com.example.hospital_management.service.IPatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/patient")
public class MedicalRecordController {
    private final IMedicalRecordService medicalRecordService;

    public MedicalRecordController(IMedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Trang form tìm kiếm
    @GetMapping("/room-search")
    public String showRoomSearchForm() {
        return "patient/room_search_form";
    }

    // Xử lý tìm kiếm phòng theo mã hồ sơ
    @GetMapping("/room-by-code")
    public String getRoomByMedicalRecordCode(@RequestParam("code") String code, Model model) {
        Room room = medicalRecordService.findRoomByCode(code);
        model.addAttribute("room", room);
        model.addAttribute("code", code);
        return "patient/room_result";
    }
}

