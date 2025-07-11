package com.example.hospital_management.controller.technicans;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.Room;
import com.example.hospital_management.service.IRoomService;
import com.example.hospital_management.service.ITestOrderService;
import com.example.hospital_management.service.ITestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/lab-technician")
public class TechnicianController {

    private final IRoomService roomService;

    private final ITestOrderService testOrderService;

    ITestReportService testReportService;

    public TechnicianController(IRoomService roomService, ITestOrderService testOrderService, ITestReportService testReportService) {
        this.roomService = roomService;
        this.testOrderService = testOrderService;
        this.testReportService = testReportService;
    }

    @GetMapping("")
    public String dashboard(Model model) {
        Employee employee = new Employee();
        employee.setEmail("lt1@hospital.com");
        List<Room> roomList = roomService.findAllTestRoom();
        model.addAttribute("roomList", roomList);
        model.addAttribute("technicianEmail", employee.getEmail());
        return "/technician/dashboard";
    }

    @GetMapping("/room/{id}")
    public String orderByRoom(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(defaultValue = "inpatient") String type,
                              @PathVariable("id") Long id,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TestRequestDto> testOrderPage;
        if ("inpatient".equals(type)) {
            testOrderPage = testOrderService.findPendingOrdersNative(id, pageable); // nội trú
        } else {
            testOrderPage = testReportService.findPendingReportsByRoom(id, pageable); // ngoại trú (cần viết thêm)
        }
        model.addAttribute("testOrderPage", testOrderPage);
        model.addAttribute("roomId", id);
        return "/technician/test_list";
    }

    @GetMapping("/patient_test/{id}")
    public String viewAllTestsByPatient(@PathVariable("id") Long patientId,
                                        @RequestParam("roomId") Long roomId,
                                        Model model) {
        List<TestRequestDto> testList = testOrderService.findAllByPatientId(patientId);
        model.addAttribute("testList", testList);
        model.addAttribute("roomId", roomId);
        return "technician/test_list_by_patient";
    }
}
