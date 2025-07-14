package com.example.hospital_management.controller.technicans;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.Room;
import com.example.hospital_management.service.IEmployeeService;
import com.example.hospital_management.service.IRoomService;
import com.example.hospital_management.service.ITestOrderService;
import com.example.hospital_management.service.ITestReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/lab-technician")
@PreAuthorize("hasAnyRole('LAB_TECHNICIAN', 'DOCTOR', 'DEPARTMENT_HEAD', 'ADMIN')")
public class TechnicianController {
    private final Cloudinary cloudinary;

    @ModelAttribute("roomList")
    public List<Room> populateRoomList() {
        return roomService.findAllTestRoom();
    }

    private final IRoomService roomService;

    private final ITestOrderService testOrderService;

    private final IEmployeeService employeeService;


    ITestReportService testReportService;

    public TechnicianController(Cloudinary cloudinary, IRoomService roomService,
                                ITestOrderService testOrderService, IEmployeeService employeeService, ITestReportService testReportService) {
        this.cloudinary = cloudinary;
        this.roomService = roomService;
        this.testOrderService = testOrderService;
        this.employeeService = employeeService;
        this.testReportService = testReportService;
    }


    @GetMapping("")
    public String dashboard(Model model, Principal principal) {
        String email = principal.getName();
        List<Room> roomList = roomService.findAllTestRoom();
        model.addAttribute("roomList", roomList);
        model.addAttribute("technicianEmail", email);
        model.addAttribute("activeMenu", "dashboard");
        return "/lab-technician/technician/dashboard";
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
        model.addAttribute("activeMenu", "room");
        model.addAttribute("type", type);
        model.addAttribute("roomId", id);
        return "/lab-technician/technician/test_list";
    }

    @GetMapping("/patient_test/{id}")
    public String viewAllTestsByPatient(@PathVariable("id") Long patientId,
                                        @RequestParam("roomId") Long roomId,
                                        @RequestParam("type") String type,
                                        Model model) {

        List<TestRequestDto> testList;
        if ("inpatient".equals(type)) {
            testList = testOrderService.findAllByPatientId(patientId);// nội trú
        } else {
            testList = testReportService.findAllByPatientId(patientId);
        }
        model.addAttribute("testList", testList);
        model.addAttribute("type", type);
        model.addAttribute("roomId", roomId);
        return "/lab-technician/technician/test_list_by_patient";
    }

    @GetMapping("/input_result")
    public String inputResultPage(@RequestParam("testOrderId") Long testOrderId,
                                  @RequestParam("roomId") Long roomId,
                                  @RequestParam("type") String type,
                                  Model model) {
        model.addAttribute("testOrderId", testOrderId);
        model.addAttribute("roomId", roomId);
        model.addAttribute("type", type);

        if ("inpatient".equals(type)) {
            testOrderService.updateTestOrderStatus(testOrderId, 3L);
        } else {
            testReportService.updateTestReportStatus(testOrderId, 3L);
        }
//        testOrderService.updateTestOrderStatus(testOrderId, 3L);
        return "lab-technician/technician/input_result";
    }

    @PostMapping("/upload-result")
    public String uploadTestResult(@RequestParam("testOrderId") Long testOrderId,
                                   @RequestParam("image") MultipartFile image,
                                   @RequestParam("note") String note,
                                   @RequestParam("roomId") Long roomId,
                                   @RequestParam("type") String type,
                                   RedirectAttributes redirectAttributes,
                                   Principal principal) {
        String email = principal.getName();
        Optional<Employee> technician = employeeService.findEmployeeByEmail(email);

        String imageUrl = "";

        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("folder", "hospital/test-results"));
            imageUrl = (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi upload ảnh: " + e.getMessage());
            return "redirect:/lab-technician/room/" + roomId + "?page=0&size=5&type=" + type;
        }

        // Gọi đúng service tùy theo loại bệnh nhân
        if ("inpatient".equals(type)) {
            testOrderService.saveTestResult(testOrderId, imageUrl, note, technician.get().getId());
            testOrderService.updateTestOrderStatus(testOrderId, 2L); // cập nhật trạng thái "đã hoàn tất"
        } else {
            testReportService.saveTestResult(testOrderId, imageUrl, note, technician.get().getId());
            testReportService.updateTestReportStatus(testOrderId, 2L);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Đã lưu kết quả thành công!");
        return "redirect:/lab-technician/room/" + roomId + "?page=0&size=5&type=" + type;
    }


    @GetMapping("/completed-today")
    public String showTodayCompletedTests(@RequestParam("roomId") Long roomId,
                                          @RequestParam(defaultValue = "inpatient") String type,
                                          Model model) {
        List<TestRequestDto> completedTests;
        Map<Long, String> imageMap = new java.util.HashMap<>();

        if ("inpatient".equals(type)) {
            completedTests = testOrderService.findTodayCompletedTests(roomId);
            for (TestRequestDto dto : completedTests) {
                String img = testOrderService.findImageByTestOrderId(dto.getId()); // method này bạn thêm ở repository
                imageMap.put(dto.getId(), img);
            }
        } else {
            completedTests = testReportService.findTodayCompletedReports(roomId);
            for (TestRequestDto dto : completedTests) {
                String img = testReportService.findImageByTestReportId(dto.getId()); // method này bạn cũng cần viết
                imageMap.put(dto.getId(), img);
            }
        }

        model.addAttribute("testList", completedTests);
        model.addAttribute("imageMap", imageMap);
        model.addAttribute("roomId", roomId);
        model.addAttribute("type", type);
        model.addAttribute("activeMenu", "completed_today");
        return "/lab-technician/technician/completed_today";
    }

}
