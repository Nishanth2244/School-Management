package com.project.student.education.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.student.education.entity.TeacherAttendance;

public interface TeacherAttendanceRepository
        extends JpaRepository<TeacherAttendance, Long> {

    Optional<TeacherAttendance> findByTeacher_TeacherIdAndAttendanceDate(
            String teacherId,
            LocalDate date);

    List<TeacherAttendance> findByTeacher_TeacherIdOrderByAttendanceDateDesc(
            String teacherId);

    List<TeacherAttendance> findByAttendanceDate(
            LocalDate date);
}