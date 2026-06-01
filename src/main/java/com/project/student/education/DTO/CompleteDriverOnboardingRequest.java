package com.project.student.education.DTO;

import lombok.Data;

@Data
public class CompleteDriverOnboardingRequest {
	
	private String fullName;
	private String password;
	private String address;
	private String experience;
	private String licenseNumber;

}
