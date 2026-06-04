package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardAnalyticsDTO {
    private String roleContext;          // e.g., "TEACHER", "PRINCIPAL", "ADMIN"
    private Integer totalActiveStudents;
    private Integer totalClassSections;
    private Double overallPassPercentage; 
    private List<ClassPerformanceDTO> classDetails;
}