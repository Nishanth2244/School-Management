package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentSubjectMarkDTO {
    private String subject;
    private Double marks;
    private Integer maxMarks;
}