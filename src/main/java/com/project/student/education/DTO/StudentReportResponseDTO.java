package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class StudentReportResponseDTO {
    private String studentId;
    private String fullName;
    private String rollNumber;
    private String classSectionId;
    private String academicYear;
    private Double totalAggregatedPercentage;
    private List<ExamPerformanceDTO> examPerformances;
}