package com.project.student.education.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.student.education.DTO.LeaveActionDTO;
import com.project.student.education.DTO.LeaveStatsResponseDTO;
import com.project.student.education.DTO.PrincipleLeaveResponseDTO;
import com.project.student.education.DTO.TeacherLeaveRequestDTO;
import com.project.student.education.DTO.TeacherOnLeaveDTO;
import com.project.student.education.ExceptionHandling.ConflictException;
import com.project.student.education.ExceptionHandling.ResourceNotFoundException;
import com.project.student.education.config.SecurityUtil;
import com.project.student.education.entity.Teacher;
import com.project.student.education.entity.TeacherLeaveRequest;
import com.project.student.education.entity.User;
import com.project.student.education.enums.LeaveStatus;
import com.project.student.education.repository.TeacherLeaveRequestRepository;
import com.project.student.education.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveService {
	
	private final SecurityUtil securityUtil;
	private final TeacherLeaveRequestRepository teacherLeaveRequestRepository;
	private final TeacherRepository teacherRepository;

	public String applyLeave(TeacherLeaveRequestDTO teacherLeaveRequestDTO) {
		
		User user = securityUtil.getCurrentUser();
		
		Teacher teacher = teacherRepository.findByUser(user) 
                .orElseThrow(() -> new IllegalArgumentException("Teacher profile not found for this user."));
		

		if (teacherLeaveRequestDTO.getEndDate().isBefore(teacherLeaveRequestDTO.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before Start date.");
        }
		
		long overlaps = teacherLeaveRequestRepository.countOverlappingLeaves(teacher.getTeacherId(), teacherLeaveRequestDTO.getStartDate(), teacherLeaveRequestDTO.getEndDate());
        if (overlaps > 0) {
            throw new IllegalArgumentException("You already have a Pending or Approved leave during these dates.");
        }
        
        
        TeacherLeaveRequest teacherLeaveRequest = new TeacherLeaveRequest();
        
        teacherLeaveRequest.setTeacher(teacher);
        teacherLeaveRequest.setReason(teacherLeaveRequestDTO.getReason());
        teacherLeaveRequest.setLeaveType(teacherLeaveRequestDTO.getLeaveType());        
        teacherLeaveRequest.setStartDate(teacherLeaveRequestDTO.getStartDate());
        teacherLeaveRequest.setEndDate(teacherLeaveRequestDTO.getEndDate());        
        
        teacherLeaveRequestRepository.save(teacherLeaveRequest);
        
		return "Leave request submitted succesfully";
	}

	public String reviewLeave(LeaveActionDTO leaveActionDTO, Long id) {
		
		User principal = securityUtil.getCurrentUser();
		
		TeacherLeaveRequest teacherLeaveRequest = teacherLeaveRequestRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Leave Not Found to Take Action"));
		
		
		if(teacherLeaveRequest.getLeaveStatus() != LeaveStatus.PENDING) {
			throw new ConflictException("This leave request is already " + teacherLeaveRequest.getLeaveStatus());
		}

		if(leaveActionDTO.getLeaveStatus() == LeaveStatus.PENDING) {
			throw new ConflictException("Action must be APPROVED or REJECTED.");
		}
		
		
		teacherLeaveRequest.setLeaveStatus(leaveActionDTO.getLeaveStatus());
		teacherLeaveRequest.setRemarks(leaveActionDTO.getRemarks());
		teacherLeaveRequest.setUser(principal);
		
		teacherLeaveRequestRepository.save(teacherLeaveRequest);

		return "Leave request " + teacherLeaveRequest.getLeaveStatus().name() + " successfully.";
		
	}

	
	public LeaveStatsResponseDTO getStats() {
		
		long pending = teacherLeaveRequestRepository.countByLeaveStatus(LeaveStatus.PENDING);
		long rejected = teacherLeaveRequestRepository.countByLeaveStatus(LeaveStatus.REJECTED);
		
		List<TeacherLeaveRequest> todayLeaves = teacherLeaveRequestRepository.findLeavesForToday(
				LocalDate.now(), 
				LeaveStatus.APPROVED
		);
		
		List<TeacherOnLeaveDTO> todayLeaveDetails = todayLeaves.stream()
				.map(leave -> new TeacherOnLeaveDTO(
						leave.getTeacher().getTeacherName(),
						leave.getLeaveType().name(),
						leave.getReason()
				))
				.toList();
		
		long onLeaveTodayCount = todayLeaveDetails.size();
		
		return new LeaveStatsResponseDTO(
		        pending, 
		        onLeaveTodayCount, 
		        rejected, 
		        todayLeaveDetails
		);
	}

	public List<PrincipleLeaveResponseDTO> getLeavesByStatus(LeaveStatus status) {
		
		List<TeacherLeaveRequest> techLeaveRequests = teacherLeaveRequestRepository.findByLeaveStatusOrderByApplieDateTimeDesc(status);

		return techLeaveRequests.stream()
				.map(techLeaveRequest -> PrincipleLeaveResponseDTO.builder()
						.leaveId(techLeaveRequest.getId())
						.teacherName(techLeaveRequest.getTeacher().getTeacherName())
						.leaveType(techLeaveRequest.getLeaveType())
						.startDate(techLeaveRequest.getStartDate())
						.endDate(techLeaveRequest.getEndDate())
						.reason(techLeaveRequest.getReason())
						.leaveStatus(techLeaveRequest.getLeaveStatus())
						.appliedOn(techLeaveRequest.getApplieDateTime())
						.build())
				.toList();
						
	}
	
	
	public List<PrincipleLeaveResponseDTO> getMyLeaveHistory() {
		
        User loggedInUser = securityUtil.getCurrentUser();

        Teacher teacher = teacherRepository.findByUser(loggedInUser)
                .orElseThrow(() -> new IllegalArgumentException("Teacher profile not found for this user."));

        List<TeacherLeaveRequest> history = teacherLeaveRequestRepository.findByTeacher_TeacherIdOrderByApplieDateTimeDesc(teacher.getTeacherId());

        return history.stream()
				.map(histor -> PrincipleLeaveResponseDTO.builder()
						.leaveId(histor.getId())
						.teacherName(histor.getTeacher().getTeacherName())
						.leaveType(histor.getLeaveType())
						.startDate(histor.getStartDate())
						.endDate(histor.getEndDate())
						.reason(histor.getReason())
						.leaveStatus(histor.getLeaveStatus())
						.appliedOn(histor.getApplieDateTime())
						.build())
				.toList();
    }


}
