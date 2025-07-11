package com.example.hospital_management.controller.patient;

import com.example.hospital_management.entity.Ticket;
import com.example.hospital_management.service.IMedicalRecordService;
import com.example.hospital_management.service.IPatientService;
import com.example.hospital_management.service.ITicketService;
import com.example.hospital_management.service.impl.EmailService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/patient")
@PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'NURSE', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class PatientController {
    private ITicketService ticketService;
    private IMedicalRecordService medicalRecordService;
    private IPatientService patientService;
    private EmailService emailService;


    public PatientController(ITicketService ticketService, IMedicalRecordService medicalRecordService, IPatientService patientService, EmailService emailService) {
        this.ticketService = ticketService;
        this.medicalRecordService = medicalRecordService;
        this.patientService = patientService;
        this.emailService = emailService;
    }

    // xử lí lấy ticket trực tiếp
    @PostMapping("/register-today")
    public String registerToday(@RequestParam String name,
                                @RequestParam String idCard,
                                @RequestParam String phone,
                                @RequestParam(required = false) String email,
                                RedirectAttributes redirect) {
        Ticket ticket = ticketService.registerTicket(name, idCard, phone, email, LocalDate.now(), false);
//        model.addAttribute("ticket", ticket);
        redirect.addFlashAttribute("successMessage",
                "Đăng ký thành công. Số thứ tự của bạn là: " + ticket.getQueueNumber());
        return "redirect:/patient";

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

        redirect.addFlashAttribute("successMessage",
                "Bạn đã đặt lịch thành công cho ngày " + appointmentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        + ". Số thứ tự ưu tiên là: " + ticket.getQueueNumber());
        return "redirect:/patient";
    }


    // Trang mặc định
    @GetMapping()
    public String homeTickets(Model model) {
//        model.addAttribute("appointmentForm", new AppointmentFormDTO());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        model.addAttribute("maxDate", LocalDate.now().plusDays(7));
        return "home";
    }


    // Xử lý tìm kiếm phòng theo mã hồ sơ
//    @GetMapping("/showroom")
//    public String getRoomByMedicalRecordCode(@RequestParam("recordId") String code, Model model) {
//        try {
//            MedicalRecord medicalRecord = medicalRecordService.findRoomByCode(code);
//            model.addAttribute("room", medicalRecord.getRoom());
//            model.addAttribute("medicalRecord", medicalRecord);
//            return "patient/room_result";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "patient/form_record";
//        }
//    }

    // xem kết quả khám
//    @GetMapping("/exam-resut")
//    public String viewExamResult(@RequestParam("recordId") String code, Model model) {
//        Optional<MedicalRecord> medicalRecord = medicalRecordService.findById(code);
//        if (medicalRecord.isPresent()) {
//            model.addAttribute("record", medicalRecord.get());
//            return "patient/exam_result";
//        } else {
//            model.addAttribute("notFount", true);
//            return "patient/medical_record_list";
//        }
//    }

    // tìm mã hồ sơ theo căn cước công dân bệnh nhân
    @GetMapping("/records/search")
    public String showSearchForm() {
        return "patient/form_record";
    }

//    @PostMapping("/records")
//    public String searchByIdCard(@RequestParam("idCard") String idCard, Model model) {
//        List<Patient> patients = patientService.findAllByIdCard(idCard);
//        if (patients.isEmpty()) {
//            model.addAttribute("notFound", true);
//            return "patient/form_record";
//        }
//        Patient patient = patients.get(0);
//        List<MedicalRecord> medicalRecords = medicalRecordService.findByPatient(patient);
//        model.addAttribute("medicalRecords", medicalRecords);
//        model.addAttribute("patient", patient);
//        return "patient/medical_record_list";
//    }


    // lấy danh sách hiển thị cho lễ tân
    @GetMapping("/list")
    public String getAllListTicket(Model model) {
        List<Ticket> tickets = ticketService.getAllTodayTicketsOrdered();
        model.addAttribute("tickets", tickets);
        return "ticket/list";
    }

    @PostMapping("/call")
    public String callTicket(@RequestParam("ticketId") Long id, RedirectAttributes redirect) {
        Ticket ticket = ticketService.callTickets(id);
        if (ticket != null) {
            redirect.addFlashAttribute("message", "Đã gọi số " + ticket.getQueueNumber() + " (" + ticket.getName() + ") " + "thành công,");
        } else {
            redirect.addFlashAttribute("message", "Không tìm thấy phiếu");
        }
        return "redirect:/patient/list";
    }
}
