package com.project.student.education.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.student.education.DTO.AdminCreateRequestDTO;
import com.project.student.education.service.SuperAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/superAdmin")
@RequiredArgsConstructor
public class SuperAdminController {
	
	private final SuperAdminService superAdminService;
	
	
	@PostMapping("/addAdmin")
	public String createAdmin(@RequestBody AdminCreateRequestDTO adminCreateRequestDTO) {
		
		superAdminService.addAdmin(adminCreateRequestDTO);
		return "Admin Created Succesfully";
	}

}
