//package com.example.hospital_management.controller;
//
//
//import com.example.hospital_management.entity.Surgery;
//import com.example.hospital_management.service.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/doctor/inpatient")
//public class SurgeryController {
//    private ISurgeryService surgeryService;
//    private IEmployeeService employeeService;
//    private ISurgeryTypeService surgeryTypeService;
//    private IPatientService patientService;
//    @Autowired
//
//    public SurgeryController(ISurgeryService surgeryService, IEmployeeService employeeService, ISurgeryTypeService surgeryTypeService, IPatientService patientService) {
//        this.surgeryService = surgeryService;
//        this.employeeService = employeeService;
//        this.surgeryTypeService = surgeryTypeService;
//        this.patientService = patientService;
//    }
//
//    @GetMapping("")
//    public String searchByName(@RequestParam(required = false, defaultValue = "0") int page,
//                               @RequestParam(required = false, defaultValue = "5") int size,
//                               @RequestParam(required = false, defaultValue = "") String name,
//                               Model model) {
//        Sort sort=Sort.by(Sort.Direction.DESC,"id");
//        Pageable pageable = PageRequest.of(page, size,sort);
//        Page<Surgery> testOrders = surgeryService.findAll(pageable);
//        model.addAttribute("testOrders", testOrders);
//        model.addAttribute("name", name);
//        return "test_order/list";
//    }
//
//    @GetMapping("inpatient/test-orders")
//    private String create(Model model) {
//        model.addAttribute("test_orders", new Surgery());
//        model.addAttribute("test", surgeryTypeService.findAll());
//        model.addAttribute("employee", employeeService.findAll());
//        model.addAttribute("patient", patientService.findAll());
//        return "test_order/create";
//    }
//
//    @PostMapping("save")
//    public String create(Surgery testOrder, RedirectAttributes redirectAttributes) {
//        surgeryService.save(testOrder);
//        redirectAttributes.addFlashAttribute("success", "Thêm mới thành công");
//        return "redirect:/doctor";
//    }
//
//    @GetMapping("{id}/edit")
//    public String update(@PathVariable Long id, Model model) {
//        Optional<Surgery> testOrderOptional = surgeryService.findById(id);
//        if (testOrderOptional.isPresent()) {
//            model.addAttribute("test_order", testOrderOptional.get());  // ✅ Gán đối tượng thật
//            model.addAttribute("test", surgeryTypeService.findAll());
//            model.addAttribute("employee", employeeService.findAll());
//            model.addAttribute("patient", patientService.findAll());
//            return "test_order/update";
//        } else {
//            return "redirect:/error"; // hoặc trang báo lỗi khác
//        }
//
//    }
//
//    @PostMapping("update")
//    public String update(Surgery testOrder, RedirectAttributes redirectAttributes) {
//        surgeryService.save(testOrder);
//        redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
//        return "redirect:/doctor";
//    }
//
//    @GetMapping("{id}/delete")
//    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        surgeryService.remove(id);
//        redirectAttributes.addFlashAttribute("success", "Đã xóa thành công!");
//        return "redirect:/doctor";
//    }
//
//    @GetMapping("{id}/detail")
//    public String detail(@PathVariable Long id, Model model) {
//        Optional<Surgery> testOrderOptional = surgeryService.findById(id);
//        if (testOrderOptional.isPresent()) {
//            model.addAttribute("test_order", testOrderOptional.get());  // ✅ Gán đối tượng thật
//            return "test_order/detail";
//        } else {
//            return "redirect:/error"; // hoặc trang báo lỗi khác
//        }
//    }
//}
