package com.example.hospital_management.controller.doctor;


import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.TestOrder;
import com.example.hospital_management.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/doctor/result/view")
public class InpatientController {

    private IImpatientRecordService impatientRecordService;
    private ITestOrderService testOrderService;

    public InpatientController(IImpatientRecordService impatientRecordService, ITestOrderService testOrderService) {
        this.impatientRecordService = impatientRecordService;
        this.testOrderService = testOrderService;
    }

    @GetMapping("")
    public String searchByName(@RequestParam(required = false, defaultValue = "") String name,
                               @RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "5") int size,
                               Model model) {
        Sort sort=Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(page, size,sort);
        Page<ImpatientRecord> impatientRecords;

        if ((name != null && !name.trim().isEmpty()) ) {
            impatientRecords = impatientRecordService.searchByName(name.isBlank() ? null : name, pageable);
        } else {
            impatientRecords = impatientRecordService.findAllByStatusTrue(name,pageable);
        }

        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("name", name);
        return "result/view";
    }
    @GetMapping("/{id}/result")
    public String viewResult(@PathVariable Long id, Model model) {
        List<TestOrder> testOrders = testOrderService.findByImpatientRecordId(id);
        TestOrder testOrder=testOrderService.findById(id);
        // Kiểm tra tất cả xét nghiệm đã hoàn thành
       // boolean allCompleted = testOrders.stream().allMatch(TestOrder::getStatus);


        // Kiểm tra tất cả đều đã hoàn thành và có kết quả
        boolean allCompleted = testOrders.stream()
                .allMatch(order -> order.getStatus()
                        && order.getResult() != null
                        && !order.getResult().isBlank());

        model.addAttribute("testOrders", testOrders);
        model.addAttribute("testOrder", testOrder);
        model.addAttribute("allCompleted", allCompleted);
        return "result/detail";
    }

    @PostMapping("/{id}/discharge")
    public String dischargePatient(@PathVariable Long id) {
        ImpatientRecord record = impatientRecordService.findById(id).orElse(null);
        if (record != null) {
            System.out.println("==> Cho ra viện bệnh nhân: " + record.getId());
            record.setStatus(false); // trạng thái false = đã ra viện // sửa chỗ này k phải boolean mà thành 0 1 2
            record.setDischargeDate(LocalDate.now());
            impatientRecordService.save(record);
        }
        return "redirect:/doctor";
    }


}
