package com.project.student.education.DTO;

import com.project.student.education.enums.Role;

import lombok.Data;

@Data
public class InviteAdminRequest {

	private String email;
	private String fullName;
	private Role role;
}
