package com.example.hospital_management.controller.reception;

import com.example.hospital_management.dto.PatientInsuranceDto;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/receptionist")
public class ReceptionController {
    private static final long BASE_FEE = 200000;
    private static final double INSURANCE_DISCOUNT = 0.8;
    private final IPatientService patientService;
    private final IInsuranceService insuranceService;
    private final IDepartmentService departmentService;
    private final IRoomService roomService;
    private final IExaminationShiftService examinationShiftService;
    private final IMedicalRecordService medicalRecordService;

    @Autowired
    public ReceptionController(IPatientService patientService, IInsuranceService insuranceService,
                               IDepartmentService departmentService, IRoomService roomService,
                               IExaminationShiftService examinationShiftService, IMedicalRecordService medicalRecordService) {
        this.patientService = patientService;
        this.insuranceService = insuranceService;
        this.departmentService = departmentService;
        this.roomService = roomService;
        this.examinationShiftService = examinationShiftService;
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/patients/register")
    public String showFormAddPatient(@RequestParam(value = "departmentId", required = false) Long departmentId,
                                     Model model) {
        model.addAttribute("departments", departmentService.findAll());

        if (departmentId != null) {
            model.addAttribute("selectedDepartmentId", departmentId);
            model.addAttribute("rooms", roomService.findAllClinicRoomsByDepartment(departmentId));
        }

        model.addAttribute("patientInsuranceDto", new PatientInsuranceDto());
        model.addAttribute("insurance", new Insurance());
        return "reception/create";
    }

    @PostMapping("/patients/register/save")
    public String save(@Validated @ModelAttribute PatientInsuranceDto patientInsuranceDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        Patient patient = new Patient();
        patientInsuranceDto.validate(patientInsuranceDto, bindingResult);

        if (Boolean.TRUE.equals(patientInsuranceDto.getHasInsurance())) {
            if (insuranceService.existsByCode(patientInsuranceDto.getCode())) {
                bindingResult.rejectValue("code", "null", "Mã BHYT đã tồn tại, vui lòng kiểm tra lại.");
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("patientInsuranceDto", patientInsuranceDto);
            if (patientInsuranceDto.getDepartmentId() != null) {
                model.addAttribute("selectedDepartmentId", patientInsuranceDto.getDepartmentId());
                model.addAttribute("rooms", roomService.findAllClinicRoomsByDepartment(patientInsuranceDto.getDepartmentId()));
            }

            return "reception/create";
        }
        //tạo bệnh nhân
        BeanUtils.copyProperties(patientInsuranceDto, patient);
        patientService.save(patient);

        //tạo bảo hiểm
        if (Boolean.TRUE.equals(patientInsuranceDto.getHasInsurance())) {
            Insurance insurance = new Insurance();
            BeanUtils.copyProperties(patientInsuranceDto, insurance);
            insurance.setPatient(patient);
            insuranceService.save(insurance);
        }

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patient);
        medicalRecord.setVisitDate(LocalDate.now());
        medicalRecord.setQueueNumber(0);
        medicalRecord.setStatus(false);
        medicalRecord.setFollowupDate(null);
        medicalRecord.setConclusion(null);
        medicalRecord.setStatus(false);

        long fee;
        if (Boolean.TRUE.equals(patientInsuranceDto.getHasInsurance())) {
            fee = (long) (BASE_FEE * (1 - INSURANCE_DISCOUNT));
        } else {
            fee = BASE_FEE;
        }
        medicalRecord.setFee(fee);

        Long maxId = medicalRecordService.findMaxId();
        String code = String.format("MR-%03d", maxId + 1);
        medicalRecord.setCode(code);
        medicalRecordService.save(medicalRecord);
        Room room = roomService.findById(patientInsuranceDto.getRoomId());
        ExaminationShift examinationShift = new ExaminationShift();
        examinationShift.setRoom(room);
        examinationShift.setMedicalRecord(medicalRecord);
        examinationShiftService.save(examinationShift);
        redirectAttributes.addFlashAttribute("success", "Thêm mới thành công bệnh nhân");
        return "redirect:/receptionist";
    }
}
