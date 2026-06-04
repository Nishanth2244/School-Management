package com.project.student.education.DTO;

import com.project.student.education.enums.TeacherAttendanceStatus;
import lombok.Data;

@Data
public class MarkTeacherAttendanceRequest {

    private String teacherId;

    private TeacherAttendanceStatus status;

    private String remarks;
}