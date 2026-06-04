package com.project.student.education.DTO;

import java.time.LocalDate;

import com.project.student.education.enums.LeaveType;

import lombok.Data;

@Data
public class TeacherLeaveRequestDTO {
	
	private String reason;
	private LeaveType leaveType;
	private LocalDate startDate;
	private LocalDate endDate;

}
