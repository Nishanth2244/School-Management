package com.project.student.education.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LeaveStatsResponseDTO {
	
	private long totalRejectedLeaves;
	private long totalPendingRequests;
	private long teachersOnLeaveToday;
	
	private List<TeacherOnLeaveDTO> teachersOnLeaveTodayDetails;

}
