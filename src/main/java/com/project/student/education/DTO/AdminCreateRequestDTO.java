package com.project.student.education.DTO;

import lombok.Data;

@Data
public class AdminCreateRequestDTO {
	
	private String userName;
	private String password;
	private String email;
	private String fullName;
	private String phone;

}
