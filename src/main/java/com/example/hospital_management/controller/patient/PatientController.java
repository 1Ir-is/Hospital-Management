package com.example.hospital_management.controller.patient;

import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import com.example.hospital_management.service.impl.EmailService;
import com.example.hospital_management.web_socket.TicketWebSocketSender;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/patient")
@PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'NURSE', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class PatientController {
    private ITicketService ticketService;
    private IMedicalRecordService medicalRecordService;
    private ITestReportService testReportService;
    private EmailService emailService;
    private IExaminationShiftService examinationShiftService;
    private TicketWebSocketSender ticketWebSocketSender;


    public PatientController(ITicketService ticketService, IMedicalRecordService medicalRecordService,
                             ITestReportService testReportService, EmailService emailService, TicketWebSocketSender ticketWebSocketSender,IExaminationShiftService examinationShiftService) {
        this.ticketService = ticketService;
        this.medicalRecordService = medicalRecordService;
        this.testReportService = testReportService;
        this.emailService = emailService;
        this.ticketWebSocketSender = ticketWebSocketSender;
        this.examinationShiftService = examinationShiftService;
    }

//    public PatientController(ITicketService ticketService, IMedicalRecordService medicalRecordService, IPatientService patientService, EmailService emailService) {
//        this.ticketService = ticketService;
//        this.medicalRecordService = medicalRecordService;
//        this.patientService = patientService;
//        this.emailService = emailService;
//    }

    // xử lí lấy ticket trực tiếp
    @PostMapping("/register-today")
    public String registerToday(@RequestParam String name,
                                @RequestParam String idCard,
                                @RequestParam String phone,
                                @RequestParam(required = false) String email,
                                RedirectAttributes redirect) {
        Ticket ticket = ticketService.registerTicket(name, idCard, phone, email, LocalDate.now(), false);
        // Gửi danh sách mới sau khi đăng ký
        List<Ticket> updatedWaitingList = ticketService.findWaitingTicketsToday();
        ticketWebSocketSender.sendUpdatedWaitingList(updatedWaitingList);
        ticketWebSocketSender.sendNewTicket(ticket);

//        model.addAttribute("ticket", ticket);
        redirect.addFlashAttribute("successMessage",
                "Đăng ký thành công. Số thứ tự của bạn là: " + ticket.getQueueNumber());
        return "redirect:/";

    }

//    @GetMapping()
//    public String patientDashboard(Model model) {
//        // PATIENT - Chức năng do Duy làm:
//        // - Người khám bệnh có thể lấy số phiếu
//        // - Người khám bệnh có thể đặt lịch khám trước ngày khám
//        // - Người khám bệnh có thể xem được phòng mình sẽ được khám
//        // - Người khám bệnh có thể xem được các kết quả xét nghiệm
//        // - Người khám bệnh có thể xem được kết quả sau khi khám
//        // - Người khám bệnh có thể biết được vị trí các phòng xét nghiệm mà bác sĩ chỉ định
//        // - Người khám có thể nhận được mail thông báo ngày tái khám trước 3 ngày theo yêu cầu bác sĩ
//
//        model.addAttribute("pageTitle", "Bệnh nhân");
//        model.addAttribute("userRole", "Bệnh nhân");
//        return "patient/index";
//    }

    // xử lí lấy ticket đăng kí trước.
    @PostMapping("/register-advance")
    public String registerAdvance(@RequestParam String name,
                                  @RequestParam String idCard,
                                  @RequestParam String phone,
                                  @RequestParam(required = false) String email,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
                                  RedirectAttributes redirect) {
        Ticket ticket = ticketService.registerTicket(name, idCard, phone, email, appointmentDate, true);

        // Gửi mail nếu có email
        if (email != null && !email.isEmpty()) {
            emailService.sendAppointmentConfirmation(email, name, appointmentDate, ticket.getQueueNumber());
        }
        // Gửi danh sách mới sau khi đăng ký
        List<Ticket> updatedWaitingList = ticketService.findWaitingTicketsToday();
        ticketWebSocketSender.sendUpdatedWaitingList(updatedWaitingList);
        ticketWebSocketSender.sendNewTicket(ticket);


        redirect.addFlashAttribute("successMessage",
                "Bạn đã đặt lịch thành công cho ngày " + appointmentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        + ". Số thứ tự ưu tiên là: " + ticket.getQueueNumber()
                        + ". Mọi thông tin check Gmail để biết thêm");

        return "redirect:/";
    }


    // Trang mặc định
//    @GetMapping()
//    public String homeTickets(Model model) {
//        model.addAttribute("today", LocalDate.now());
//        model.addAttribute("minDate", LocalDate.now().plusDays(1));
//        model.addAttribute("maxDate", LocalDate.now().plusDays(7));
//        return "home";
//    }


    // tìm mã hồ sơ theo căn cước công dân bệnh nhân
    @GetMapping("/records/search")
    public String showSearchForm() {
        return "patient/record_search";
    }

    //     Xử lý tìm kiếm phòng theo CCCD của bệnh nhân
    @PostMapping("/record")
    public String listRecords(@RequestParam("idCard") String idCard, Model model, HttpSession session) {

        session.setAttribute("idCard", idCard);
        List<MedicalRecord> records = medicalRecordService.findByPatientIdCard(idCard);
        Map<Long, Room> roomMap = new HashMap<>();
        for (MedicalRecord record : records) {
            ExaminationShift shift = examinationShiftService.getByMedicalRecordId(record.getId());
            if (shift != null && shift.getRoom() != null) {
                roomMap.put(record.getId(), shift.getRoom());
            }
        }
//        if (records.isEmpty()) {
//            model.addAttribute("error", "Không tìm thấy hồ sơ khám nào với CCCD đã nhập");
//            return "index";
//        }
        model.addAttribute("records", records);
        model.addAttribute("recordRoomMap", roomMap);
        return "patient/record_list";

    }

    @GetMapping("/record")
    public String reloadRecords(Model model, HttpSession session) {
        String idCard = (String) session.getAttribute("idCard");
        if (idCard == null || idCard.trim().isEmpty()) {
            return "redirect:/patient/record";
        }
//        List<MedicalRecord> records = medicalRecordService.findByPatientIdCard(idCard);
//        if (records.isEmpty()) {
//            model.addAttribute("error", "Không tìm thấy hồ sơ khám nào");
//            return "redirect:/patient/record/list";
//        }
//        model.addAttribute("records", records);
//        "patient/record_list"
        return listRecords(idCard, model, session);
    }

    // xem phòng khám theo mã hồ sơ cá nhân
    @GetMapping("/record/{id}/room")
    public String viewRoom(@PathVariable("id") Long recordId, Model model) {
        MedicalRecord medicalRecord = medicalRecordService.findById(recordId);
        if (medicalRecord == null || medicalRecord.getExaminationShift().getRoom() == null) {
            model.addAttribute("error", "Không tìm thấy thông tin phòng khám");
            return "patient/room_view";
        }
        Room room = medicalRecord.getExaminationShift().getRoom();
        model.addAttribute("room", room);
        model.addAttribute("record", medicalRecord);
        return "patient/room_view";
    }

    // xem kết quả khám
    @GetMapping("/record/{id}/result")
    public String viewResult(@PathVariable("id") Long recordId, Model model) {
        MedicalRecord record = medicalRecordService.findById(recordId);
        if (record == null) {
            model.addAttribute("error", "Không tìm thấy hồ sơ khám");
            return "patient/record_result";
        }
        model.addAttribute("record", record);
        return "patient/record_result";
    }

    // xem kết quả phòng xét nghiệm
    @GetMapping("/record/{id}/test-rooms")
    public String viewTestRooms(@PathVariable("id") Long recordId, Model model) {
        List<TestReport> reports = testReportService.findByMedicalRecordId(recordId);
        model.addAttribute("reports", reports);
        return "patient/test_rooms";
    }


    // lấy danh sách hiển thị cho lễ tân
//    @GetMapping("/list")
//    public String getAllListTicket(Model model) {
//        List<Ticket> tickets = ticketService.getAllTodayTicketsOrdered();
//        model.addAttribute("tickets", tickets);
//        return "ticket/list";
//    }
//
//    @PostMapping("/call")
//    public String callTicket(@RequestParam("ticketId") Long id, RedirectAttributes redirect) {
//        Ticket ticket = ticketService.callTickets(id);
//        if (ticket != null) {
//            redirect.addFlashAttribute("message", "Đã gọi số " + ticket.getQueueNumber() + " (" + ticket.getName() + ") " + "thành công,");
//        } else {
//            redirect.addFlashAttribute("message", "Không tìm thấy phiếu");
//        }
//        return "redirect:/patient/list";
//    }

    @GetMapping("/waiting-room")
    public String showWaitingRoom(Model model) {
        List<Ticket> waitingTickets = ticketService.findWaitingTicketsToday();
        model.addAttribute("waitingTickets", waitingTickets);
        return "patient/waiting_room";
    }

}
