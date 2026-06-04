package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassPerformanceDTO {
    private String classSectionId;
    private String className;
    private String section;
    private String classTeacherName;
    private Integer studentCount;
    private Double classPassPercentage;
}