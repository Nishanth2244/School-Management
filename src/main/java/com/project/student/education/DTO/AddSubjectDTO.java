package com.project.student.education.DTO;

import lombok.Data;

@Data
public class AddSubjectDTO {
    private String subjectId;
    private String teacherId;
    private Integer maxMarks;
    private Integer passingMarks;
    
}