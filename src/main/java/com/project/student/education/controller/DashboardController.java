package com.project.student.education.controller;

import com.project.student.education.DTO.UserListDTO;
import com.project.student.education.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import com.project.student.education.DTO.AdminDashboardDTO;
import com.project.student.education.service.SuperAdminService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/superAdmin")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public AdminDashboardDTO getDashboard() {
        return dashboardService.getDashboard();
    }
    @PutMapping("/users/{username}/status")
    public String updateStatus(
            @PathVariable String username,
            @RequestParam Boolean active) {

        return dashboardService.updateStatus(username, active);
    }

    @GetMapping("/users")
    public List<UserListDTO> getAllUsers() {
        return dashboardService.getAllUsers();
    }
}