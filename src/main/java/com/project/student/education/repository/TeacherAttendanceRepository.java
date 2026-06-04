package com.project.student.education.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.project.student.education.enums.TeacherAttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.student.education.entity.TeacherAttendance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherAttendanceRepository
        extends JpaRepository<TeacherAttendance, Long> {

    Optional<TeacherAttendance> findByTeacher_TeacherIdAndAttendanceDate(
            String teacherId,
            LocalDate date);

    List<TeacherAttendance> findByTeacher_TeacherIdOrderByAttendanceDateDesc(
            String teacherId);


    List<TeacherAttendance> findByAttendanceDate(
            LocalDate attendanceDate);

    List<TeacherAttendance> findByAttendanceDateAndStatus(
            LocalDate attendanceDate,
            TeacherAttendanceStatus status);

    @Query("""
SELECT COUNT(t)
FROM TeacherAttendance t
WHERE t.teacher.teacherId = :teacherId
AND t.status = :status
AND MONTH(t.attendanceDate) = :month
AND YEAR(t.attendanceDate) = :year
""")
    Long countByTeacherAndStatusAndMonth(
            @Param("teacherId") String teacherId,
            @Param("status") TeacherAttendanceStatus status,
            @Param("month") int month,
            @Param("year") int year);

}