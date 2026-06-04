package com.project.student.education.DTO;

import lombok.Data;

@Data
public class StudentMarkDTO {
    private String studentId;
    private Double obtainedMarks; // Kept as Double to match your ExamMark Entity type
    private String remarks;
}