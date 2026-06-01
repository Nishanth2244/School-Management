package com.project.student.education.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.student.education.DTO.CompleteDriverOnboardingRequest;
import com.project.student.education.service.DriverService;

import lombok.RequiredArgsConstructor;

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
}
