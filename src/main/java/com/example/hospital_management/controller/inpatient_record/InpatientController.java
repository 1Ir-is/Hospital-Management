package com.example.hospital_management.controller.inpatient_record;


import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/doctor/result/view")
public class InpatientController {
    private IPatientService patientService;
    private IBedService bedService ;
    private IRoomService roomService;
    private IImpatientRecordService impatientRecordService;
    @Autowired
    public InpatientController(IPatientService patientService, IBedService bedService, IRoomService roomService, IImpatientRecordService impatientRecordService) {
        this.patientService = patientService;
        this.bedService = bedService;
        this.roomService = roomService;
        this.impatientRecordService = impatientRecordService;
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
            impatientRecords = impatientRecordService.findAll(pageable);
        }

//        if (name != null && !name.trim().isEmpty()) {
//            testReports = testReportService.searchByName(name,medicalRecordId, pageable);
//        } else {
//            testReports = testReportService.findAll(pageable);
//        }
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("patient", patientService.findAll());
        model.addAttribute("room", roomService.findAll());
        model.addAttribute("name", name);
        return "result/view";
    }
}
