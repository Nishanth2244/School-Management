package com.project.student.education.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.project.student.education.enums.LeaveStatus;
import com.project.student.education.enums.LeaveType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class TeacherLeaveRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable = false)
	private Teacher teacher;
	
	@Enumerated(EnumType.STRING)
	private LeaveType leaveType;
	
	@Enumerated(EnumType.STRING)
	private LeaveStatus leaveStatus = LeaveStatus.PENDING;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private String reason;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewed_by")
	private User user;
	
	private String remarks;
	
	private LocalDateTime applieDateTime = LocalDateTime.now();
	

}
