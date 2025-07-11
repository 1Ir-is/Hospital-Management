package com.example.hospital_management.controller;

import com.example.hospital_management.dto.MedicalRecordDto;
import com.example.hospital_management.dto.TestSummaryDto;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/examination")
public class ExaminationController {
    private final IMedicalRecordService medicalRecordService;
    private final ITestService testService;
    private final ITestReportService testReportService;
    private final IEmployeeService employeeService;
    private final IExaminationShiftService examinationShiftService;
    private final IRoomService roomService;

    public ExaminationController(IMedicalRecordService medicalRecordService, ITestService testService, ITestReportService testReportService, IEmployeeService employeeService, IExaminationShiftService examinationShiftService, IRoomService roomService) {
        this.medicalRecordService = medicalRecordService;
        this.testService = testService;
        this.testReportService = testReportService;
        this.employeeService = employeeService;
        this.examinationShiftService = examinationShiftService;
        this.roomService = roomService;
    }

    @GetMapping("/room")
    public String roomList(Model model) {
//        List<ExaminationShift> examinationShifts = examinationShiftService.findAll();
        List<Room> roomList = roomService.findAllExaminationRoom();
        model.addAttribute("roomList", roomList);
        return "/examination/room-list";
    }

    @GetMapping("/room/{roomId}")
    public String chooseRoom(Model model, @PathVariable("roomId") Long room_id, HttpSession session) {
        Employee employee = employeeService.findById(1L).get();
        session.setAttribute("roomId", room_id);
        session.setAttribute("employee", employee);
        return "redirect:/examination";
    }

//    @GetMapping("")
//    public String detail(Model model,
//                         @PageableDefault(size = 5, page = 0) Pageable pageable) {
//        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getWaitingRecords(pageable);

    /// /        MedicalRecordDto medicalRecordDto = medicalRecordService.getCurrentPatient();
//
//        model.addAttribute("medicalRecords", medicalRecords);
//        model.addAttribute("medicalRecord", null);
//        return "/examination/detail";
//
//    }
//
//    @GetMapping("/next")
//    public String nextPatient(Model model,
//                              @PageableDefault(size = 5, page = 0) Pageable pageable) {
//        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getWaitingRecords(pageable);
//        MedicalRecordDto medicalRecordDto = medicalRecordService.getCurrentPatient();
//
//        model.addAttribute("medicalRecords", medicalRecords);
//        if (medicalRecordDto != null) {
//            model.addAttribute("medicalRecord", medicalRecordDto);
//            return "/examination/detail";
//        }
//        return "/examination/list";
//    }
//
//    @GetMapping("/list")
//    public String examinationList(Model model,
//                                  @PageableDefault(size = 5, page = 0) Pageable pageable) {
//        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getWaitingRecords(pageable);
//        model.addAttribute("medicalRecords", medicalRecords);
//        return "/examination/list";
//    }
    @GetMapping("")
    public String detail(Model model, @PageableDefault(size = 5, page = 0) Pageable pageable, HttpSession session) {
        Long roomId = (Long) session.getAttribute("roomId");
        if (roomId == null) {
            return "redirect:/examination/room";
        }
        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getWaitingRecords(pageable, roomId);

        model.addAttribute("medicalRecords", medicalRecords);
        model.addAttribute("medicalRecord", null);
        model.addAttribute("doctor", employeeService.findById(1L).get());
        return "/examination/detail";

    }

    @GetMapping("/next")
    public String nextPatient(Model model, @PageableDefault(size = 5, page = 0) Pageable pageable, HttpSession session) {
        if (session.getAttribute("roomId") == null) {
            return "redirect:/examination/room";
        }
        Long roomId = (Long) session.getAttribute("roomId");


        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getWaitingRecords(pageable, roomId);
        MedicalRecordDto medicalRecordDto = medicalRecordService.getCurrentPatient(roomId);

        model.addAttribute("medicalRecords", medicalRecords);
        model.addAttribute("doctor", employeeService.findById(1L).get());
        if (medicalRecordDto != null) {
            model.addAttribute("medicalRecord", medicalRecordDto);
            return "/examination/detail";
        }
        return "/examination/list";
    }

    @GetMapping("/list")
    public String examinationList(Model model, @PageableDefault(size = 5, page = 0) Pageable pageable, HttpSession session) {
        Long roomId = (Long) session.getAttribute("roomId");
        if (roomId == null) {
            return "redirect:/examination/room";
        }
//        Room room =  roomService.findById(roomId).get();
        MedicalRecordDto medicalRecordDto = medicalRecordService.getCurrentPatient(roomId);
        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getWaitingRecords(pageable, roomId);
        model.addAttribute("medicalRecords", medicalRecords);
//        model.addAttribute("roomInfo", medicalRecords.getContent().get(0));
        model.addAttribute("doctor", employeeService.findById(1L).get());
        model.addAttribute("medicalRecord", medicalRecordDto);
        return "/examination/list";
    }

    @PostMapping("/complete")
    public String completeExamination(@RequestParam("id") Long id, @RequestParam("note") String note) {
        //User tạm thời
        Employee employee = employeeService.findById(1L).get();

        MedicalRecord medicalRecord = medicalRecordService.findById(id);
        if (medicalRecord != null) {
            ExaminationShift shift = examinationShiftService.findByMedicalRecord(medicalRecord);
            shift.setEmployee(employee);
            medicalRecord.setStatus(true);
            medicalRecord.setNote(note);
            medicalRecordService.save(medicalRecord);
        }
        return "redirect:/examination";
    }

    @GetMapping("/{id}/test/list")
    private String testOrder(@PathVariable Long id, Model model) {
        MedicalRecord medicalRecord = medicalRecordService.findById(id);
        model.addAttribute("medicalRecord", medicalRecord);
        List<Test> testList = testService.findAll();
        model.addAttribute("testList", testList);
        return "/examination/test-order";
    }

    @PostMapping("/{id}/test/save")
    public String sendOrder(@PathVariable Long id, @RequestParam("chosenTestIds") List<Long> chosenIds, Model model) {
        if (chosenIds.isEmpty()) {
            return "redirect:/examination";
        }
        MedicalRecord medicalRecord = medicalRecordService.findById(id);

        for (Long tId : chosenIds) {
            Test test = testService.findById(tId).get();
            TestReport testReport = new TestReport(null, null, LocalDate.now(), false, null, null, false, medicalRecord, test, null, null);
            testReportService.save(testReport);
        }
        medicalRecord.setStatus(true);
        medicalRecordService.save(medicalRecord);
        return "redirect:/examination";
    }

    @GetMapping("/test/queue")
    public String testingQueue(Model model, @PageableDefault Pageable pageable) {
        Page<TestSummaryDto> testSummaryDtos = medicalRecordService.getTestingMedicalRecordList(pageable);
        model.addAttribute("testSummaries", testSummaryDtos.getContent());
        return "/examination/test-list";
    }

    @GetMapping("/test/detail/{medicalRecordId}")
    public String testDetail(@PathVariable Long medicalRecordId, Model model) {
        MedicalRecord medicalRecord = medicalRecordService.findById(medicalRecordId);
        List<TestReport> testReportList = testReportService.findByMedicalRecordId(medicalRecord.getId());
        model.addAttribute("medicalRecord", medicalRecord);
        model.addAttribute("testReportList", testReportList);
        return "/examination/test-detail";
    }
}
