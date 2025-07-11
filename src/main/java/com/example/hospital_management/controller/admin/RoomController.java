package com.example.hospital_management.controller.admin;

import com.example.hospital_management.dto.RoomDTO;
import com.example.hospital_management.entity.Department;
import com.example.hospital_management.entity.RoomType;
import com.example.hospital_management.repository.IDepartmentRepository;
import com.example.hospital_management.repository.IRoomTypeRepository;
import com.example.hospital_management.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final IRoomService roomService;
    private final IDepartmentRepository departmentRepository;
    private final IRoomTypeRepository roomTypeRepository;

    @GetMapping
    public String listRooms(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(defaultValue = "number") String sortBy,
                            @RequestParam(defaultValue = "asc") String sortDir,
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) Integer number,
                            @RequestParam(required = false) Long departmentId,
                            @RequestParam(required = false) Long roomTypeId,
                            @RequestParam(required = false) Boolean status,
                            Model model) {

        // Create pageable object
        Sort.Direction direction = sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Get rooms with filters
        Page<RoomDTO> rooms;
        if (name != null || number != null || departmentId != null || roomTypeId != null || status != null) {
            rooms = roomService.searchRooms(name, number, departmentId, roomTypeId, status, pageable);
        } else {
            rooms = roomService.getAllRooms(pageable);
        }

        // Get filter options
        List<Department> departments = departmentRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        List<RoomType> roomTypes = roomTypeRepository.findAllOrderByName();

        // Get statistics
        Long totalRooms = roomService.getTotalRoomCount();
        Long activeRooms = roomService.getActiveRoomCount();

        // Add to model
        model.addAttribute("activeMenu", "rooms");
        model.addAttribute("rooms", rooms);
        model.addAttribute("departments", departments);
        model.addAttribute("roomTypes", roomTypes);
        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("activeRooms", activeRooms);
        model.addAttribute("inactiveRooms", totalRooms - activeRooms);

        // Keep filter values
        model.addAttribute("currentName", name);
        model.addAttribute("currentNumber", number);
        model.addAttribute("currentDepartmentId", departmentId);
        model.addAttribute("currentRoomTypeId", roomTypeId);
        model.addAttribute("currentStatus", status);
        model.addAttribute("currentSortBy", sortBy);
        model.addAttribute("currentSortDir", sortDir);
        model.addAttribute("currentSize", size);

        return "admin/room/list";
    }

    @GetMapping("/{id}")
    public String viewRoom(@PathVariable Long id, Model model) {
        try {
            RoomDTO room = roomService.getRoomById(id);
            model.addAttribute("activeMenu", "rooms");
            model.addAttribute("room", room);
            return "admin/room/detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/admin/rooms";
        }
    }
}