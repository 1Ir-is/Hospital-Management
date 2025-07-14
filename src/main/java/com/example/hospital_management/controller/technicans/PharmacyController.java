package com.example.hospital_management.controller.technicans;

import com.example.hospital_management.dto.IPrescriptionDetailDto;
import com.example.hospital_management.dto.PrescriptionRequestDto;
import com.example.hospital_management.entity.Prescription;
import com.example.hospital_management.service.IPrescriptionDetailService;
import com.example.hospital_management.service.IPrescriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/pharmacy")
@PreAuthorize("hasAnyRole('PHARMACY_STAFF', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class PharmacyController {
    private final IPrescriptionService prescriptionService;
    private final IPrescriptionDetailService prescriptionDetailService;


    public PharmacyController(IPrescriptionService prescriptionService, IPrescriptionDetailService prescriptionDetailService) {
        this.prescriptionService = prescriptionService;
        this.prescriptionDetailService = prescriptionDetailService;
    }

//    @GetMapping()
//    public String pharmacyDashboard(Model model) {
//        // PHARMACY_STAFF - Chức năng do Nhơn làm:
//        // - Nhân viên phòng vật tư và thiết bị y tế có thể cấp thuốc cho bệnh nhân theo đơn thuốc điều trị của bác sĩ
//
//        model.addAttribute("pageTitle", "Phòng vật tư");
//        model.addAttribute("userRole", "Nhân viên phòng vật tư");
//        return "pharmacy/index";
//    }

    @GetMapping("/paid-prescriptions")
    public String showPaidPrescriptions(@RequestParam("code") String code, Model model) {
        List<PrescriptionRequestDto> prescriptions =
                prescriptionService.findAllPaidPrescriptionsByMedicalRecordCode(code);
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("code", code);
        return "/pharmacy/display/paid-prescriptions";
    }

    @GetMapping("")
    public String showPharmacy(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PrescriptionRequestDto> prescriptions = prescriptionService.getAllPrescriptions(pageable);
        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("prescriptions", prescriptions);
        return "/pharmacy/display/dashboard";
    }

    @GetMapping("/prescription/{id}")
    public String showPrescriptionDetail(@PathVariable Long id, Model model) {
        Prescription prescription = prescriptionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuốc"));

        model.addAttribute("prescription", prescription);
        List<IPrescriptionDetailDto> details = prescriptionDetailService.getDetailsByPrescriptionId(id);
        model.addAttribute("prescription", prescription);
        model.addAttribute("details", details);
        return "pharmacy/display/detail"; // trang HTML để hiển thị
    }


    @PostMapping("/confirm/{id}")
    public String confirmPrescription(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Prescription prescription = prescriptionService.findById(id).orElseThrow();

        if (!prescription.getStatus()) {
            prescription.setStatus(true);
            prescription.setCreatedDate(LocalDate.now());
            prescriptionService.save(prescription);
            redirectAttributes.addFlashAttribute("success", "Đơn thuốc đã được cấp.");
        } else {
            redirectAttributes.addFlashAttribute("info", "Đơn thuốc đã được cấp trước đó.");
        }

        return "redirect:/pharmacy";
    }

    @PostMapping("/next")
    public String callNextPatient(RedirectAttributes redirectAttributes) {
        Optional<PrescriptionRequestDto> nextPrescription = prescriptionService.findFirstUnprocessedPrescription();
        if (nextPrescription.isPresent()) {
            return "redirect:/pharmacy/prescription/" + nextPrescription.get().getId();
        }
        redirectAttributes.addFlashAttribute("info", "Không còn đơn thuốc nào cần cấp.");
        return "redirect:/pharmacy";
    }

    @GetMapping("/dispensed-today")
    public String showTodayDispensedPrescriptions(Model model) {
        List<PrescriptionRequestDto> prescriptions = prescriptionService.findTodayDispensedPrescriptions();
        model.addAttribute("activeMenu", "dashboard"); // hoặc đặt khác nếu muốn menu đổi sáng
        model.addAttribute("prescriptions", prescriptions);
        return "pharmacy/display/dispensed_today"; // bạn tạo thêm file HTML hiển thị danh sách này
    }


}
