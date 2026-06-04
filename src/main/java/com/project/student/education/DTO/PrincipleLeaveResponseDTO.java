package com.project.student.education.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.project.student.education.enums.LeaveStatus;
import com.project.student.education.enums.LeaveType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrincipleLeaveResponseDTO {
	
	private Long leaveId;
	private String teacherName;
	private LeaveType leaveType;
	private LocalDate startDate;
	private LocalDate endDate;
	private String reason;
	private LeaveStatus leaveStatus;
	private LocalDateTime appliedOn;

}
