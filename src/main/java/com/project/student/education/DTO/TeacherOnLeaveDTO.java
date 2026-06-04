package com.project.student.education.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherOnLeaveDTO {
    private String teacherName;
    private String leaveType;
    private String reason;
}