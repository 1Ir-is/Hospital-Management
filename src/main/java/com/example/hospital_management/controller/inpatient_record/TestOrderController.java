package com.example.hospital_management.controller.inpatient_record;


import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.Test;
import com.example.hospital_management.entity.TestOrder;
import com.example.hospital_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/doctor")
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
    public String searchByName(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "5") int size,
                               @RequestParam(required = false, defaultValue = "") String name,
                               Model model) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ImpatientRecord> impatientRecords = impatientRecordService.findAll(pageable);
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("name", name);
        return "test_order/list";
    }


    @GetMapping("{id}/edit")
    public String update(@PathVariable Long id, Model model) {
        TestOrder order = testOrderService.findById(id);
        // ⚠ Kiểm tra tránh null
        if (order.getImpatientRecord() == null) {
            return "redirect:/error"; // hoặc báo lỗi rõ ràng
        }

        model.addAttribute("test_order", order);
        model.addAttribute("test", testService.findAll());
        model.addAttribute("employee", employeeService.findAll());
        model.addAttribute("patient", patientService.findAll());
        return "test_order/update";
    }


    @PostMapping("/{id}/test/save")
    public String sendOrder(@PathVariable Long id,
                            @RequestParam("chosenTestIds") List<Long> chosenIds,
                            Model model) {
        if (chosenIds.isEmpty()) {
            return "redirect:/doctor";
        }
        ImpatientRecord impatientRecord = impatientRecordService.findById(id).get();
        impatientRecord.setStatus(true); // chuyển trạng thái khi chọn sang đang chờ xét nghiệm
        for (Long tId : chosenIds) {
            Test test = testService.findById(tId).get();
            TestOrder testOrder = new TestOrder(
                    null,                    // id
                    LocalDate.now(),         // date
                    null,                    // note
                    null,                    // result
                    false,                   // status
                    false,                   // payStatus
                    null,                    // imagePath
                    impatientRecord,         // impatientRecord
                    null,                    // employee
                    test,                    // test
                    null                     // testStatus
            );

            testOrderService.save(testOrder);

        }
        impatientRecordService.save(impatientRecord);
        return "redirect:/doctor";
    }

//    @GetMapping("{id}/delete")
//    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        testOrderService.remove(id);
//        redirectAttributes.addFlashAttribute("success", "Đã xóa thành công!");
//        return "redirect:/doctor";
//    }


    @GetMapping("{id}/detail")
    public String detail(@PathVariable Long id, Model model) {
        TestOrder testOrders = testOrderService.findById(id);
        List<TestOrder> testOrderOptional = testOrderService.findByImpatientRecordId(id);
        model.addAttribute("test_orders", testOrders);
        model.addAttribute("test_order", testOrderOptional);
        return "test_order/detail";
    }

    @GetMapping("/test-order/{id}/result")
    public String viewResult(@PathVariable Long id, Model model) {
        List<TestOrder> testOrder = testOrderService.findByImpatientRecordId(id);
        TestOrder testOrders = testOrderService.findById(id);
        model.addAttribute("testOrder", testOrder);
        model.addAttribute("testOrders", testOrders);
        return "test_order/result-detail"; // trỏ đến file HTML mới
    }

}
