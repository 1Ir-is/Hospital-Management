package com.example.hospital_management.controller.admin;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.service.IExaminationShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/current-sessions")
@RequiredArgsConstructor
public class AdminExaminationShiftController {
    private final IExaminationShiftService examinationShiftService;

    @GetMapping
    public String listCurrentShifts(Model model,
                                    @RequestParam(defaultValue = "0") int page) {
        int size = 5;
        Page<ExaminationShift> shifts = examinationShiftService.getCurrentShifts(page, size);
        model.addAttribute("shifts", shifts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", shifts.getTotalPages());
        model.addAttribute("activeMenu", "current-sessions");
        return "admin/examination_shift/list";
    }
}
