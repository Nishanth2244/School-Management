package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentFeeSummaryDTO {
    private String studentId;
    private String studentName;
//    private String rollNumber; // Optional: helps admin identify students quickly
    private double totalDue;
    private double totalPaid;
    private double remainingBalance;
    private String status; // Calculated field: "PAID", "PARTIAL", or "UNPAID"
}