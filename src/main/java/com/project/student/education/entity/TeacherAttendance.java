package com.project.student.education.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.project.student.education.enums.TeacherAttendanceStatus;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TeacherAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    private TeacherAttendanceStatus status;

    private String remarks;

    private LocalDateTime markedAt = LocalDateTime.now();
}