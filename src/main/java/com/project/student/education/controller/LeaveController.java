package com.project.student.education.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.student.education.DTO.LeaveActionDTO;
import com.project.student.education.DTO.LeaveStatsResponseDTO;
import com.project.student.education.DTO.PrincipleLeaveResponseDTO;
import com.project.student.education.DTO.TeacherLeaveRequestDTO;
import com.project.student.education.enums.LeaveStatus;
import com.project.student.education.service.LeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teacher/leave")
public class LeaveController {
	
	private final LeaveService leaveService;
	
	
	@PostMapping("/applyLeave")
	public String applyLeave(@RequestBody TeacherLeaveRequestDTO teacherLeaveRequestDTO) {
		
		return leaveService.applyLeave(teacherLeaveRequestDTO);
	}
	
	
	@PutMapping("/action")
	public String reviewLeave(@RequestBody LeaveActionDTO leaveActionDTO,
							@RequestParam Long id) {
		
		return leaveService.reviewLeave(leaveActionDTO, id);
	}
	
	
	@GetMapping("/getStats")
	private LeaveStatsResponseDTO getleaveStats() {
		return leaveService.getStats();
	}

	
	@GetMapping("/byStatus")
	public List<PrincipleLeaveResponseDTO> getLeavesByStatus(@RequestParam LeaveStatus status){
		
		return leaveService.getLeavesByStatus(status);
	}
	
	
//	Teacher
	@GetMapping("/history")
	public List<PrincipleLeaveResponseDTO>getTeacherLeaveHistory() {

		return leaveService.getMyLeaveHistory();
	}
}
