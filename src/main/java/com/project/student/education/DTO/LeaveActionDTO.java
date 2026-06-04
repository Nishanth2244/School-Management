package com.project.student.education.DTO;

import com.project.student.education.enums.LeaveStatus;

import lombok.Data;

@Data
public class LeaveActionDTO {
	
	private String remarks;
	private LeaveStatus leaveStatus;

}
