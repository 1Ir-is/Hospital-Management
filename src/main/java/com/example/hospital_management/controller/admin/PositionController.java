package com.example.hospital_management.controller.admin;

import com.example.hospital_management.entity.Position;
import com.example.hospital_management.service.IPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/positions")
public class PositionController {

    @Autowired
    private IPositionService positionService;

    @GetMapping()
    public String showListPositions(Model model) {
        List<Position> positions = positionService.findAllPositions();
        model.addAttribute("positions", positions);
        return "admin/position/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Position position = new Position();
        model.addAttribute("position", position);
        return "admin/position/create-form";
    }

    @PostMapping("/create")
    public String createPosition(@ModelAttribute("position") Position position) {
        positionService.savePosition(position);
        return "redirect:/admin/positions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Position position = positionService.findPositionById(id);
        if (position == null) {
            return "redirect:/admin/positions";
        }
        model.addAttribute("position", position);
        return "admin/position/edit-form";
    }

    @PostMapping("/edit/{id}")
    public String editPosition(@PathVariable Long id, @ModelAttribute("position") Position position) {
        position.setId(id);
        positionService.savePosition(position);
        return "redirect:/admin/positions";
    }

    @GetMapping("/delete/{id}")
    public String deletePosition(@PathVariable Long id) {
        positionService.deletePositionById(id);
        return "redirect:/admin/positions";
    }
}
