package com.project.student.education.DTO;

import lombok.Data;
import java.util.List;

@Data
public class ClassSectionRequest {

    private String className;
    private String section;
    private String academicYear;
    private Integer capacity;
    
    private String classTeacherId;

    
    private List<String> subjectIds;
    private  Integer currentStrength;
}