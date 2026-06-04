package com.project.student.education.DTO;

import lombok.Data;

@Data
public class ExamSubjectDTO {
    private String examSubjectId;
    private String examId;
    private String subjectId;
    private String teacherId;
    private Integer maxMarks;
    private Integer passingMarks;
}