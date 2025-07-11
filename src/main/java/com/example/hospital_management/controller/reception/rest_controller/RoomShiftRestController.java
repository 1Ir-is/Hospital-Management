package com.example.hospital_management.controller.reception.rest_controller;

import com.example.hospital_management.entity.Room;
import com.example.hospital_management.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class RoomShiftRestController {
    private final IRoomService roomService;

    @Autowired
    public RoomShiftRestController(IRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public List<Room> getRooms(@RequestParam Long departmentId) {
        return roomService.findAllClinicRoomsByDepartment(departmentId);
    }

}
