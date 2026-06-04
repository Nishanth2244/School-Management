package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherSubjectResponseDTO {
    private String examSubjectId;
    private String examName;
    private String subjectId;
    private Integer maxMarks;
}