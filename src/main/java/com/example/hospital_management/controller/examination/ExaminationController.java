package com.example.hospital_management.controller.examination;

import com.example.hospital_management.dto.MedicalRecordDto;
import com.example.hospital_management.dto.TestSummaryDto;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final IExaminationShiftStatusService examinationShiftStatusService;
    private final IRoomService roomService;
    private final IImpatientRecordService impatientRecordService;

    public ExaminationController(IMedicalRecordService medicalRecordService, ITestService testService, ITestReportService testReportService, IEmployeeService employeeService, IExaminationShiftService examinationShiftService, IExaminationShiftStatusService examinationShiftStatusService, IRoomService roomService, IImpatientRecordService impatientRecordService) {
        this.medicalRecordService = medicalRecordService;
        this.testService = testService;
        this.testReportService = testReportService;
        this.employeeService = employeeService;
        this.examinationShiftService = examinationShiftService;
        this.examinationShiftStatusService = examinationShiftStatusService;
        this.roomService = roomService;
        this.impatientRecordService = impatientRecordService;
    }


    @GetMapping("/room")
    public String roomList(Model model) {
//        List<ExaminationShift> examinationShifts = examinationShiftService.findAll();
        List<Room> roomList = roomService.findAllExaminationRoom();
        model.addAttribute("roomList", roomList);
        return "/examination/room-list";
    }

    @GetMapping("/room/{roomId}")
    public String chooseRoom(Model model, @PathVariable("roomId") Long roomId,
                             HttpSession session,
                             @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Employee employee = employeeService.findByEmail(email);
        session.setAttribute("roomId", roomId);
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
        medicalRecordService.getTestingMedicalRecordList(pageable, roomId);

        Employee employee = (Employee) session.getAttribute("employee");
        if(employee == null){
            return "redirect:/examination/room";
        }


        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getAllStatusRecords(pageable, roomId);

        model.addAttribute("medicalRecords", medicalRecords);
        model.addAttribute("medicalRecord", null);
        model.addAttribute("employee", employee);
        return "/examination/detail";

    }

    @GetMapping("/next")
    public String nextPatient(Model model, @PageableDefault(size = 5, page = 0) Pageable pageable, HttpSession session) {
        if (session.getAttribute("roomId") == null) {
            return "redirect:/examination/room";
        }

        Employee employee = (Employee) session.getAttribute("employee");
        if(employee == null){
            return "redirect:/examination/room";
        }
        Long roomId = (Long) session.getAttribute("roomId");

        medicalRecordService.getTestingMedicalRecordList(pageable, roomId);

        MedicalRecordDto medicalRecordDto = medicalRecordService.getCurrentPatient(roomId);
        if (medicalRecordDto == null) {
            model.addAttribute("errorMessage", "Hiện không có bệnh nhân cần khám");
            return "redirect:/examination";
        }

        ExaminationShift shift = examinationShiftService.findByMedicalRecordId(medicalRecordDto.getId());
        shift.setExaminationShiftStatus(examinationShiftStatusService.findById(2L));

        examinationShiftService.save(shift);

        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getAllStatusRecords(pageable, roomId);


        model.addAttribute("medicalRecords", medicalRecords);
        model.addAttribute("employee", employee);
        model.addAttribute("medicalRecord", medicalRecordDto);

        return "/examination/detail";
    }

    @GetMapping("/list")
    public String examinationList(Model model, @PageableDefault(size = 5, page = 0) Pageable pageable, HttpSession session) {
        Long roomId = (Long) session.getAttribute("roomId");
        if (roomId == null) {
            return "redirect:/examination/room";
        }

        Employee employee = (Employee) session.getAttribute("employee");
//        if(employee == null){
//            return "redirect:/examination/room";
//        }
//        Room room =  roomService.findById(roomId).get();
        MedicalRecordDto medicalRecordDto = medicalRecordService.getCurrentPatient(roomId);
        Page<MedicalRecordDto> medicalRecords = medicalRecordService.getWaitingRecords(pageable, roomId);

        if(medicalRecordDto == null){
            model.addAttribute("medicalRecord", new MedicalRecordDto());
        }else{
            model.addAttribute("medicalRecord", medicalRecordDto);
        }

        model.addAttribute("medicalRecords", medicalRecords);
//        model.addAttribute("roomInfo", medicalRecords.getContent().get(0));
        model.addAttribute("employee", employee);

        return "/examination/list";
    }

    @PostMapping("/complete")
    public String completeExamination(@RequestParam("id") Long id,
                                      @RequestParam(required = false) String note,
                                      @RequestParam(required = false) String conclusion,
                                      RedirectAttributes attributes,
                                      HttpSession session) {
        //User tạm thời
        Employee employee = (Employee) session.getAttribute("employee");
        if(employee == null){
            return "redirect:/examination/room";
        }

        MedicalRecord medicalRecord = medicalRecordService.findById(id);
        if (medicalRecord != null) {
            ExaminationShift shift = examinationShiftService.findByMedicalRecord(medicalRecord);
            shift.setEmployee(employee);
                shift.setExaminationShiftStatus(examinationShiftStatusService.findById(5L));

            if(note != null){
                medicalRecord.setNote(note);
            } else if (conclusion != null) {
                medicalRecord.setConclusion(conclusion);
            }

            medicalRecordService.save(medicalRecord);
            examinationShiftService.save(shift);
            attributes.addFlashAttribute("success", "Hoàn tất khám!");
        }
        return "redirect:/examination";
    }

    @PostMapping("/{id}/test/list")
    private String testOrder(@PathVariable Long id, Model model,
                             @RequestParam(required = false) String note,
                             @RequestParam(required = false) String conclusion) {
        MedicalRecord medicalRecord = medicalRecordService.findById(id);

        if(note != null){
            medicalRecord.setNote(note);
        } else if (conclusion != null) {
            medicalRecord.setConclusion(conclusion);
        }

        medicalRecordService.save(medicalRecord);
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

        TestStatus testStatus = new TestStatus();
        testStatus.setId(1L);

        for (Long tId : chosenIds) {
            Test test = testService.findById(tId).get();
            TestReport testReport = new TestReport(null, null, LocalDate.now(), false, null, null, false, medicalRecord, test, null, testStatus);
            testReportService.save(testReport);
        }
        ExaminationShift shift = examinationShiftService.findByMedicalRecord(medicalRecord);
        shift.setExaminationShiftStatus(examinationShiftStatusService.findById(3L));
        examinationShiftService.save(shift);
//        medicalRecord.setStatus(true);

//        medicalRecordService.save(medicalRecord);
        return "redirect:/examination";
    }

    @GetMapping("/test/waiting")
    public String testingQueue(Model model, @PageableDefault Pageable pageable, HttpSession session) {
        Long roomId = (Long) session.getAttribute("roomId");
        Page<TestSummaryDto> testSummaryDtos = medicalRecordService.getTestingMedicalRecordList(pageable, roomId);
        model.addAttribute("testSummaries", testSummaryDtos.getContent());
        return "/examination/test-list";
    }

    @GetMapping("/test/detail/{medicalRecordId}")
    public String testDetail(@PathVariable Long medicalRecordId, Model model) {
        MedicalRecord medicalRecord = medicalRecordService.findById(medicalRecordId);
        ExaminationShift shift = examinationShiftService.findByMedicalRecord(medicalRecord);
        List<TestReport> testReportList = testReportService.findByMedicalRecordId(medicalRecord.getId());

        if(shift.getExaminationShiftStatus().getId().equals(4L)){
            model.addAttribute("isReady", true);
        }else{
            model.addAttribute("isReady", false);
        }
        model.addAttribute("medicalRecord", medicalRecord);
        model.addAttribute("testReportList", testReportList);
        return "/examination/test-detail";
    }

    @GetMapping("/{id}/createInpatient")
    public String formInpatientRecord(@PathVariable("id") Long medicalRecordId, Model model){
        MedicalRecord medicalRecord = medicalRecordService.findById(medicalRecordId);
        model.addAttribute("medicalRecord", medicalRecord);
        return "/examination/inpatient-form";
    }

    @PostMapping("/{id}/saveNote")
    public String saveNote(@PathVariable("id") Long medicalRecordId,
                           @RequestParam(required = false) String note,
                           @RequestParam(required = false) String conclusion){
        MedicalRecord medicalRecord = medicalRecordService.findById(medicalRecordId);
        if(note != null){
            medicalRecord.setNote(note);
        } else if (conclusion != null) {
            medicalRecord.setConclusion(conclusion);
        }

        medicalRecordService.save(medicalRecord);
        return "redirect:/examination/" + medicalRecordId + "/createInpatient";
    }

    @PostMapping("/{id}/createInpatient")
    public String saveInpatientRecord(@PathVariable("id") Long medicalRecordId,
                                      @RequestParam("reason") String reason){
        MedicalRecord medicalRecord = medicalRecordService.findById(medicalRecordId);


        ImpatientRecord impatientRecord = new ImpatientRecord();
        impatientRecord.setReason(reason);
        impatientRecord.setMedicalRecord(medicalRecord);
        impatientRecord.setStatus(false);

        ExaminationShift shift = examinationShiftService.findByMedicalRecord(medicalRecord);
        shift.setExaminationShiftStatus(examinationShiftStatusService.findById(5L));

        examinationShiftService.save(shift);
        impatientRecordService.save(impatientRecord);
        return "redirect:/examination";
    }

}
