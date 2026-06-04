package com.project.student.education.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.student.education.entity.TeacherLeaveRequest;
import com.project.student.education.enums.LeaveStatus;

@Repository
public interface TeacherLeaveRequestRepository extends JpaRepository<TeacherLeaveRequest, Long>{
	
	@Query("SELECT COUNT(l) FROM TeacherLeaveRequest l WHERE l.teacher.id = :teacherId " +
	           "AND l.leaveStatus IN ('PENDING', 'APPROVED') " +
	           "AND (l.startDate <= :endDate AND l.endDate >= :startDate)")
	long countOverlappingLeaves(@Param("teacherId") String teacherId, 
	                            @Param("startDate") LocalDate startDate, 
	                            @Param("endDate") LocalDate endDate);

	long countByLeaveStatus(LeaveStatus pending);

	List<TeacherLeaveRequest> findByLeaveStatusOrderByApplieDateTimeDesc(LeaveStatus status);

	List<TeacherLeaveRequest> findByTeacher_TeacherIdOrderByApplieDateTimeDesc(String teacherId);
	
	@Query("SELECT l FROM TeacherLeaveRequest l WHERE l.leaveStatus = :status AND :today BETWEEN l.startDate AND l.endDate")
	List<TeacherLeaveRequest> findLeavesForToday(@Param("today") LocalDate today, @Param("status") LeaveStatus status);
	
//	long countTeachersOnLeaveToday(LocalDate now);
}
