package com.example.hospital_management.controller.doctor;


import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.Test;
import com.example.hospital_management.entity.TestOrder;
import com.example.hospital_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
@PreAuthorize("hasAnyRole('DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class TestOrderController {
    private ITestOrderService testOrderService;
    private IEmployeeService employeeService;
    private ITestService testService;
    private IPatientService patientService;
    private IImpatientRecordService impatientRecordService;


    @Autowired
    public TestOrderController(ITestOrderService testOrderService, IEmployeeService employeeService, ITestService testService, IPatientService patientService, IImpatientRecordService impatientRecordService) {
        this.testOrderService = testOrderService;
        this.employeeService = employeeService;
        this.testService = testService;
        this.patientService = patientService;
        this.impatientRecordService = impatientRecordService;
    }

    @GetMapping("")
    public String welcome(){
        return "doctor/index";
    }


    @GetMapping("inpatient-list")
    public String searchByName(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "5") int size,
                               @RequestParam(required = false, defaultValue = "") String name,
                               Model model) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ImpatientRecord> impatientRecords = impatientRecordService.findAllByStatusTrue(name,pageable);
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("name", name);
        return "doctor/list";
    }


//    @GetMapping("{id}/edit")
//    public String update(@PathVariable Long id, Model model) {
//        TestOrder order = testOrderService.findById(id);
//        // ⚠ Kiểm tra tránh null
//        if (order.getImpatientRecord() == null) {
//            return "redirect:/error"; // hoặc báo lỗi rõ ràng
//        }
//
//        model.addAttribute("test_order", order);
//        model.addAttribute("test", testService.findAll());
//        model.addAttribute("employee", employeeService.findAll());
//        model.addAttribute("patient", patientService.findAll());
//        return "doctor/update";
//    }


//    @PostMapping("/{id}/test/save")
//    public String sendOrder(@PathVariable Long id,
//                            @RequestParam("chosenTestIds") List<Long> chosenIds,
//                            Model model) {
//        if (chosenIds.isEmpty()) {
//            return "redirect:/doctor";
//        }
//        ImpatientRecord impatientRecord = impatientRecordService.findById(id).get();
//        impatientRecord.setStatus(true); // chuyển trạng thái khi chọn sang đang chờ xét nghiệm
//        for (Long tId : chosenIds) {
//            Test test = testService.findById(tId).get();
//            TestOrder testOrder = new TestOrder(
//                    null,                    // id
//                    LocalDate.now(),         // date
//                    null,                    // note
//                    null,                    // result
//                    false,                   // status
//                    false,                   // payStatus
//                    null,                    // imagePath
//                    impatientRecord,         // impatientRecord
//                    null,                    // employee
//                    test,                    // test
//                    null                     // testStatus
//            );
//
//            testOrderService.save(testOrder);
//
//        }
//        impatientRecordService.save(impatientRecord);
//        return "redirect:/doctor";
//    }


    @GetMapping("{id}/edit")
    public String update(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model) {
        TestOrder order = testOrderService.findById(id);
        // Kiểm tra tránh null
        if (order == null || order.getImpatientRecord() == null) {
            return "redirect:/error"; // Hoặc báo lỗi rõ ràng
        }

        // Lấy nhân viên từ email đăng nhập
        String email = userDetails.getUsername();
        Employee employee = employeeService.findByEmail(email);

        // Thêm thông tin vào model
        model.addAttribute("test_order", order);
        model.addAttribute("test", testService.findAll());
        model.addAttribute("employee", employee); // Nhân viên đăng nhập
        model.addAttribute("employeeName", employee != null ? employee.getName() : "Không xác định");
        model.addAttribute("patient", patientService.findAll());

        return "doctor/update";
    }


    @PostMapping("/{id}/test/save")
    public String sendOrder(@PathVariable("id") Long impatientRecordId,
                            @RequestParam("chosenTestIds") String chosenTestIds,
                            @AuthenticationPrincipal UserDetails userDetails) {

        if (chosenTestIds == null || chosenTestIds.trim().isEmpty()) {
            return "redirect:/doctor/inpatient-list";
        }

        // Lấy thông tin hồ sơ nội trú
        ImpatientRecord impatientRecord = impatientRecordService.findById(impatientRecordId).orElse(null);
        if (impatientRecord == null) {
            return "redirect:/doctor/inpatient-list";
        }

        // Lấy nhân viên từ tài khoản đăng nhập (qua email)
        String email = userDetails.getUsername();
        Employee employee = employeeService.findByEmail(email);

        // Đánh dấu hồ sơ đang chờ xét nghiệm
        impatientRecord.setStatus(true);

        // Chuyển testId thành danh sách Long
        List<Long> testIds = Arrays.stream(chosenTestIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        for (Long testId : testIds) {
            Test test = testService.findById(testId).orElse(null);
            if (test != null) {
                TestOrder testOrder = new TestOrder(
                        null,                     // id
                        LocalDate.now(),          // date
                        null,                     // note
                        null,                     // result
                        false,                    // status
                        false,                    // payStatus
                        null,                     // imagePath
                        impatientRecord,          // impatientRecord
                        employee,                 // employee (người tạo)
                        test,                     // test
                        null                      // testStatus
                );
                testOrderService.save(testOrder);
            }
        }

        impatientRecordService.save(impatientRecord);
        return "redirect:/doctor/inpatient-list";
    }



    @GetMapping("{id}/detail")
    public String detail(@PathVariable Long id, Model model) {
        TestOrder testOrders = testOrderService.findById(id);
        List<TestOrder> testOrderOptional = testOrderService.findByImpatientRecordId(id);
        model.addAttribute("test_orders", testOrders);
        model.addAttribute("test_order", testOrderOptional);
        return "doctor/detail";
    }

    @GetMapping("/test-order/{id}/result")
    public String viewResult(@PathVariable Long id, Model model) {
        List<TestOrder> testOrder = testOrderService.findByImpatientRecordId(id);
        TestOrder testOrders = testOrderService.findById(id);
        model.addAttribute("testOrder", testOrder);
        model.addAttribute("testOrders", testOrders);
        return "doctor/result-detail"; // trỏ đến file HTML mới
    }

    //post này để thêm cái result sau khi hoàn thành xét nghiệm
    @PostMapping("/{id}/result/update")
    public String updateResult(@PathVariable Long id, @RequestParam String result) {
        TestOrder order = testOrderService.findById(id);
        if (order != null) {
            order.setResult(result);
            testOrderService.save(order);
        }
        // Quay lại trang detail theo bệnh nhân
        assert order != null;
        return "redirect:/doctor/" + order.getImpatientRecord().getId() + "/detail";
    }

}
