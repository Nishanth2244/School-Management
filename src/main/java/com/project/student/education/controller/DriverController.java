package com.project.student.education.controller;

import com.project.student.education.DTO.DriverResponseDTO;
import com.project.student.education.entity.Driver;
import org.springframework.web.bind.annotation.*;

import com.project.student.education.DTO.CompleteDriverOnboardingRequest;
import com.project.student.education.service.DriverService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {
	
	private final DriverService driverService;

	@PostMapping("/complete-onboarding")
    public String completeOnboardingz(@RequestBody CompleteDriverOnboardingRequest request,
    									@RequestParam String token) {
		
        return driverService.completeDriverOnboarding(request, token);
    }

    @GetMapping("/all")
    public List<DriverResponseDTO> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @PutMapping("/{driverId}")
    public DriverResponseDTO updateDriver(
            @PathVariable String driverId,
            @RequestBody DriverResponseDTO request) {

        return driverService.updateDriver(driverId, request);
    }

    @DeleteMapping("/{driverId}")
    public String deleteDriver(
            @PathVariable String driverId) {

        return driverService.deleteDriver(driverId);
    }

}
