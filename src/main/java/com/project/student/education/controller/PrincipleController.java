package com.project.student.education.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.student.education.DTO.CompleteOnboardingRequestDTO;
import com.project.student.education.DTO.InviteDriverRequest;
import com.project.student.education.config.SecurityUtil;
import com.project.student.education.service.DriverService;
import com.project.student.education.service.SuperAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/principle")
@RequiredArgsConstructor
public class PrincipleController {
	
	private final SuperAdminService superAdminService;
	private final DriverService driverService;
	private final SecurityUtil securityUtil;

	@PostMapping("/complete-onboarding")
	public String completeOnboarding(@RequestBody CompleteOnboardingRequestDTO request,
									@RequestParam String token) {
	    String responseMessage = superAdminService.completeOnboarding(request, token);
	    return responseMessage;
	}
	
	
	@PostMapping("/invite-driver")
    public String inviteDriver( @RequestBody InviteDriverRequest request,
    							@RequestParam (defaultValue = "driver") String role) {
		
		Long userId = securityUtil.getCurrentUserId();
        return driverService.inviteDriver(userId, request);
    }
}
