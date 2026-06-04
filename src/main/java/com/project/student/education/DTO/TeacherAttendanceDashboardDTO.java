package com.project.student.education.DTO;

import lombok.Data;

@Data
public class TeacherAttendanceDashboardDTO {

    private String teacherId;
    private String teacherName;

    private int month;
    private int year;

    private Long presentCount;
    private Long absentCount;
    private Long leaveCount;
}