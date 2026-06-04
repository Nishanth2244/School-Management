package com.project.student.education.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherAttendanceResponseDTO {

    private Long id;
    private String teacherId;
    private String teacherName;
    private LocalDate attendanceDate;
    private String status;
    private String remarks;
}