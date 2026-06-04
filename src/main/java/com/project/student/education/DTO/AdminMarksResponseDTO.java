package com.project.student.education.DTO;

import com.project.student.education.enums.ExamAttendanceStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminMarksResponseDTO {
    private String markId;
    private String studentId;
    private String studentName;
    private String subjectId;
    private Double obtainedMarks;
    private ExamAttendanceStatus attendanceStatus;
}