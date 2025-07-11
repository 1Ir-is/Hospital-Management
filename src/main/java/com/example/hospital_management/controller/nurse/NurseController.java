package com.example.hospital_management.controller.nurse;

import com.example.hospital_management.dto.AdvancePaymentDto;
import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.service.IAdvancePaymentService;
import com.example.hospital_management.service.IEmployeeService;
import com.example.hospital_management.service.impl.ImpatientRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/nurse")
public class NurseController {
    private final ImpatientRecordService impatientRecordService;
    private final IAdvancePaymentService iAdvancePaymentService;
    private final IEmployeeService iEmployeeService;

    public NurseController(ImpatientRecordService impatientRecordService, IAdvancePaymentService iAdvancePaymentService, IEmployeeService iEmployeeService) {
        this.impatientRecordService = impatientRecordService;
        this.iAdvancePaymentService = iAdvancePaymentService;
        this.iEmployeeService = iEmployeeService;
    }

    @ModelAttribute("sizeList")
    public List<Integer> sizeList() {
        return Arrays.asList(5, 10, 15, 20, 25);
    }

    @ModelAttribute("impatientRecordList")
    public List<ImpatientRecord> impatientRecords() {
        return impatientRecordService.findAll();
    }

    @GetMapping("")
    public String findAll(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String patientName,
            @RequestParam(required = false, defaultValue = "1") Long employeeId,
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

}
