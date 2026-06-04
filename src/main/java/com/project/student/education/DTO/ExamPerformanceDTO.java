package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ExamPerformanceDTO {
    private String examId;
    private String examName;
    private Double examPercentage;
    private String overallResultStatus; // e.g., PASS, FAIL
    private List<ParentSubjectMarkDTO> subjectMarks; 
}