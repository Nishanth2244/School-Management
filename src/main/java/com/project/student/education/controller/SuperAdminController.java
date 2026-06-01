package com.project.student.education.controller;

import com.project.student.education.config.SecurityUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.student.education.DTO.AdminCreateRequestDTO;
import com.project.student.education.DTO.InviteAdminRequest;
import com.project.student.education.DTO.InviteDriverRequest;
import com.project.student.education.config.SecurityUtil;
import com.project.student.education.service.SuperAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/superAdmin")
@RequiredArgsConstructor
public class SuperAdminController {

	private final SuperAdminService superAdminService;
	private final SecurityUtil securityUtil;

	@PostMapping("/addAdmin")
	public String createAdmin(@RequestBody AdminCreateRequestDTO adminCreateRequestDTO) {

		superAdminService.addAdmin(adminCreateRequestDTO);
		return "Admin Created Succesfully";
	}

	@PostMapping("/invitePrinciple")
	public String inviteAdmin(@RequestBody InviteAdminRequest inviteAdminRequest) {

		Long userId = securityUtil.getCurrentUserId();
		superAdminService.sendInvite(userId, inviteAdminRequest);
		return "Invite link succesfully send to the: " + inviteAdminRequest.getEmail();
	}

}
