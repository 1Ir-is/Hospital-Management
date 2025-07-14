package com.example.hospital_management.controller.reception;

import com.example.hospital_management.dto.ImpatientRecordDto;
import com.example.hospital_management.dto.PatientInsuranceDto;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import com.example.hospital_management.web_socket.TicketWebSocketSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/receptionist")
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'NURSE', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class ReceptionController {
    private static final long BASE_FEE = 200000;
    private static final double INSURANCE_DISCOUNT = 0.8;
    private final IPatientService patientService;
    private final IInsuranceService insuranceService;
    private final IDepartmentService departmentService;
    private final IRoomService roomService;
    private final IExaminationShiftService examinationShiftService;
    private final IMedicalRecordService medicalRecordService;
    private final IBedService iBedService;
    private final IRoomService iRoomService;
    private final IMedicalRecordService iMedicalRecordService;
    private final IImpatientRecordService impatientRecordService;
    private final ITicketService ticketService;
    private final TicketWebSocketSender ticketWebSocketSender;
    private final IExaminationShiftStatusService examinationShiftStatusService;
    @Autowired
    public ReceptionController(IPatientService patientService, IInsuranceService insuranceService,
                               IDepartmentService departmentService, IRoomService roomService,
                               IExaminationShiftService examinationShiftService, IMedicalRecordService medicalRecordService, IBedService iBedService, IRoomService iRoomService, IMedicalRecordService iMedicalRecordService, IImpatientRecordService impatientRecordService, ITicketService ticketService, TicketWebSocketSender ticketWebSocketSender, IExaminationShiftStatusService examinationShiftStatusService) {
        this.patientService = patientService;
        this.insuranceService = insuranceService;
        this.departmentService = departmentService;
        this.roomService = roomService;
        this.examinationShiftService = examinationShiftService;
        this.medicalRecordService = medicalRecordService;
        this.iBedService = iBedService;
        this.iRoomService = iRoomService;
        this.iMedicalRecordService = iMedicalRecordService;
        this.impatientRecordService = impatientRecordService;
        this.ticketService = ticketService;
        this.ticketWebSocketSender = ticketWebSocketSender;
        this.examinationShiftStatusService = examinationShiftStatusService;
    }


    @ModelAttribute("bedList")
    public List<Bed> bedList() {
        return iBedService.getListBedNotUsage();
    }

    @GetMapping("/patients/register")
    public String showFormAddPatient(@RequestParam(value = "departmentId", required = false) Long departmentId,
                                     @RequestParam(value = "ticketId", required = false) Long ticketId,
                                     Model model) {
        model.addAttribute("departments", departmentService.findAll());

        if (departmentId != null) {
            model.addAttribute("selectedDepartmentId", departmentId);
            model.addAttribute("rooms", roomService.findAllClinicRoomsByDepartment(departmentId));
        }
        PatientInsuranceDto patientInsuranceDto = new PatientInsuranceDto();

        // 👇 Gán queueNumber nếu có ticketId
        if (ticketId != null) {
            Ticket ticket = ticketService.findById(ticketId);
            if (ticket != null) {
                patientInsuranceDto.setQueueNumber(ticket.getQueueNumber()); // thêm field queueNumber trong PatientInsuranceDto
                model.addAttribute("ticketInfo", ticket); // để hiện thị trên view nếu muốn
            }
        }
        model.addAttribute("patientInsuranceDto", patientInsuranceDto);
        model.addAttribute("insurance", new Insurance());
        return "reception/create";
    }

    @ModelAttribute("sizeList")
    public List<Integer> sizeList() {
        return Arrays.asList(5, 10, 15, 20, 25);
    }

    @ModelAttribute("roomList")
    public List<Room> roomList() {
        return iRoomService.findAll();
    }

    @GetMapping("")
    public String getListWaitingToImpatient(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String patientName,
            @RequestParam(required = false, defaultValue = "") String code,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ImpatientRecord> impatientRecords = impatientRecordService.findAllWaitingToImpatient(patientName, code, pageable);
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("patientName", patientName);
        model.addAttribute("size", size);
        model.addAttribute("code", code);
        return "reception/list-admission";
    }

    @PostMapping("/patients/register/save")
    public String save(@Validated @ModelAttribute PatientInsuranceDto patientInsuranceDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        Patient patient = new Patient();
        patientInsuranceDto.validate(patientInsuranceDto, bindingResult);

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
//        medicalRecord.setQueueNumber(0);
        medicalRecord.setStatus(false);
        medicalRecord.setFollowupDate(null);
        medicalRecord.setConclusion(null);
        medicalRecord.setStatus(false);
        medicalRecord.setQueueNumber(patientInsuranceDto.getQueueNumber() != null ? patientInsuranceDto.getQueueNumber() : 0);

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
        examinationShift.setExaminationShiftStatus(examinationShiftStatusService.findById(1L));
        examinationShift.setMedicalRecord(medicalRecord);
        examinationShiftService.save(examinationShift);
        redirectAttributes.addFlashAttribute("success", "Thêm mới thành công bệnh nhân");
        return "redirect:/receptionist/list";
    }

    @GetMapping("/admission/getBed")
    @ResponseBody
    public List<Map<String, Object>> getBedsByRoomId(@RequestParam("roomId") Integer roomId) {
        List<Bed> beds = iBedService.findAvailableBedsByRoomId(roomId);
        return beds.stream().map(b -> {
            Map<String, Object> bedMap = new HashMap<>();
            bedMap.put("id", b.getId());
            bedMap.put("number", b.getNumber());
            return bedMap;
        }).collect(Collectors.toList());
    }

    @PostMapping("/admissions/{id}/create")
    public String postAdmissions(@Validated @ModelAttribute("impatientRecordDto") ImpatientRecordDto impatientRecordDto, @PathVariable Long id, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        ImpatientRecord impatientRecord = new ImpatientRecord();

        new ImpatientRecordDto().validate(impatientRecordDto, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("impatientRecordDto", impatientRecordDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.impatientRecordDto", bindingResult);
            return "redirect:/receptionist/admissions/" + id + "/create";
        }
        BeanUtils.copyProperties(impatientRecordDto, impatientRecord);
        impatientRecord.setStatus(true);
        impatientRecord.setId(id);
        impatientRecordService.save(impatientRecord);
        return "redirect:/receptionist";
    }

    @GetMapping("/admissions/{id}/create")
    public String getFormAdmissions(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("impatientRecordDto")) {
            ImpatientRecord impatientRecord = impatientRecordService.getImpatientRecordById(id);
            ImpatientRecordDto dto = new ImpatientRecordDto();
            dto.setMedicalRecord(impatientRecord.getMedicalRecord());
            dto.setReason(impatientRecord.getReason());
            model.addAttribute("impatientRecordDto", dto);
        } else {
            ImpatientRecordDto dto = (ImpatientRecordDto) model.getAttribute("impatientRecordDto");

            if (dto.getMedicalRecord() != null) {
                if (dto.getMedicalRecord().getExaminationShift() == null) {
                    Long medicalRecordId = dto.getMedicalRecord().getId();
                    if (medicalRecordId != null) {
                        dto.setMedicalRecord(iMedicalRecordService.getMedicalRecordById(medicalRecordId));
                    }
                }
            }
        }
        return "/reception/create-admission";
    }

    // lấy danh sách hiển thị cho lễ tân
    @GetMapping("/list")
    public String getAllListTicket(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   Model model) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Ticket> tickets = ticketService.getAllTodayTicketsOrdered(pageable);
        model.addAttribute("tickets", tickets);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", tickets.getTotalPages());
        return "ticket/list";
    }

    @PostMapping("/call")
    public String callTicket(@RequestParam("ticketId") Long id, RedirectAttributes redirect) {
        Ticket ticket = ticketService.callTickets(id);
        if (ticket != null) {
            ticketWebSocketSender.sendCurrentCalledTicket(ticket);

            // Gửi danh sách chờ mới
            List<Ticket> updatedWaiting = ticketService.findWaitingTicketsToday();
            ticketWebSocketSender.sendUpdatedWaitingList(updatedWaiting);
            ticketWebSocketSender.sendNewTicket(ticket); // Tùy mục đích

            redirect.addFlashAttribute("message", "Đã gọi số " + ticket.getQueueNumber() + " (" + ticket.getName() + ") thành công");
            return "redirect:/receptionist/patients/register?ticketId=" + ticket.getId();
        } else {
            redirect.addFlashAttribute("message", "Không tìm thấy phiếu");
            return "redirect:/patient/list";
        }
    }


    @GetMapping("/patients/today-records")
    public String viewTodayRecords(@RequestParam(required = false) Long statusId,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ExaminationShift> examinationShifts;

        if (statusId != null) {
            examinationShifts = examinationShiftService.getTodayRecordsByStatus(statusId, pageable);
        } else {
            examinationShifts = examinationShiftService.getTodayRecords(pageable); // lấy tất cả
        }

        model.addAttribute("examinationShifts", examinationShifts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", examinationShifts.getTotalPages());
        model.addAttribute("statuses", examinationShiftStatusService.findAll());
        model.addAttribute("statusId", statusId); // để hiển thị lại select
        return "reception/today_records";
    }

}
