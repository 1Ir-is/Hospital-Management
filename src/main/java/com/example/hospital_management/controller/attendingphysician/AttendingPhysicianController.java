package com.example.hospital_management.controller.attendingphysician;


import com.example.hospital_management.dto.InpatientTreatmentDto;
import com.example.hospital_management.entity.*;
import com.example.hospital_management.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor/attending-physician")
public class AttendingPhysicianController {
    public final IImpatientRecordService impatientRecordService;
    public final IInpatientTreatmentService iInpatientTreatmentService;
    public final IMedicineService iMedicineService;
    public final IEmployeeAssignmentService employeeAssignmentService;
    public final IEmployeeService employeeService;

    public AttendingPhysicianController(IImpatientRecordService impatientRecordService, IInpatientTreatmentService iInpatientTreatmentService, IMedicineService iMedicineService, IEmployeeAssignmentService employeeAssignmentService, IEmployeeService employeeService) {
        this.impatientRecordService = impatientRecordService;
        this.iInpatientTreatmentService = iInpatientTreatmentService;
        this.iMedicineService = iMedicineService;
        this.employeeAssignmentService = employeeAssignmentService;
        this.employeeService = employeeService;
    }

    @ModelAttribute("sizeList")
    public List<Integer> sizeList() {
        return Arrays.asList(5, 10, 15, 20, 25);
    }

    @ModelAttribute("medicineList")
    public List<Medicine> getListMedicine(){
        return iMedicineService.getAll();
    }

    @ModelAttribute("nurses")
    public List<Employee> nurseList(){
        return employeeService.findNurse();
    }
    @ModelAttribute("locationList")
    public List<String> getListLocation(){
        List<String> locationList = new ArrayList<>();
        locationList.add("Bệnh viện");
        locationList.add("Tại nhà");
        return locationList;
    }

    @GetMapping("")
    public String getAllImpatientRecord(
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "") String patientName,
            @RequestParam(required = false, defaultValue = "") String code,
            Model model){
        Pageable pageable = PageRequest.of(page, size);
        Page<ImpatientRecord> impatientRecords = impatientRecordService.findAllImpatientRecordsList(patientName,code, pageable);
        Map<Long, List<Employee>> assignedNursesMap = new HashMap<>();
        for (ImpatientRecord ir : impatientRecords) {
            List<EmployeeAssignment> assignments = employeeAssignmentService.findByInpatientRecordId(ir.getId());
            List<Employee> nurses = assignments.stream()
                    .map(EmployeeAssignment::getEmployee)
                    .collect(Collectors.toList());
            assignedNursesMap.put(ir.getId(), nurses);
        }
        model.addAttribute("assignedNursesMap", assignedNursesMap);
        model.addAttribute("impatientRecords", impatientRecords);
        model.addAttribute("patientName", patientName);
        model.addAttribute("code", code);
        model.addAttribute("size", size);
        return "attending_physician/list";
    }


    @PostMapping("/assign")
    public String assignNurse(@RequestParam Long inpatientRecordId,
                              @RequestParam Long employeeId, RedirectAttributes redirectAttributes){
        employeeAssignmentService.deleteByInpatientRecordId(inpatientRecordId);
        EmployeeAssignment employeeAssignment = new EmployeeAssignment();
        employeeAssignment.setEmployee(employeeService.findEmployeeById(employeeId));
        employeeAssignment.setImpatientRecord(impatientRecordService.getImpatientRecordById(inpatientRecordId));
        employeeAssignmentService.save(employeeAssignment);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công!");
        return "redirect:/doctor/attending-physician";
    }

    @GetMapping("/{id}/create")
    public String setInpatientTreatment( @PathVariable Long id, Model model){
        if(!model.containsAttribute("inpatientTreatmentDto")){
        ImpatientRecord impatientRecord = impatientRecordService.getImpatientRecordById(id);
        InpatientTreatmentDto inpatientTreatmentDto = new InpatientTreatmentDto();
        inpatientTreatmentDto.setImpatientRecord(impatientRecord);
        model.addAttribute("inpatientTreatmentDto",inpatientTreatmentDto);}
        else{
            InpatientTreatmentDto dto = (InpatientTreatmentDto) model.getAttribute("inpatientTreatmentDto");
            assert dto != null;
            dto.setImpatientRecord(impatientRecordService.getImpatientRecordById(id));
        }
        List<InpatientTreatment> treatmentHistory = iInpatientTreatmentService.findByImpatientRecordId(id);
        List<EmployeeAssignment> employeeAssignments = employeeAssignmentService.findAll(id);
        model.addAttribute("employeeAssignments", employeeAssignments);
        model.addAttribute("treatmentHistory", treatmentHistory);
        if (model.containsAttribute("showModal")) {
            model.addAttribute("openModal", true);
        }
        return "attending_physician/create";
    }

    @PostMapping("/{id}/create")
    public String postInpatientTreatment(
            @Validated @ModelAttribute("inpatientTreatmentDto")InpatientTreatmentDto inpatientTreatmentDto, BindingResult bindingResult,
            @PathVariable Long id, RedirectAttributes redirectAttributes){
        InpatientTreatment inpatientTreatment = new InpatientTreatment();
        new InpatientTreatmentDto().validate(inpatientTreatmentDto,bindingResult);
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("inpatientTreatmentDto", inpatientTreatmentDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.inpatientTreatmentDto", bindingResult);
            redirectAttributes.addFlashAttribute("showModal", true);
            return "redirect:/doctor/attending-physician/" + id + "/create";
        }
        BeanUtils.copyProperties(inpatientTreatmentDto,inpatientTreatment);
        iInpatientTreatmentService.save(inpatientTreatment);
        redirectAttributes.addFlashAttribute("successMessage", "Tạo đơn thuốc thành công!");
        return "redirect:/doctor/attending-physician/" + id + "/create";
    }



}
