package com.project.student.education.DTO;

import lombok.Data;
import java.util.List;

@Data
public class SubmitMarksDTO {
    private String examSubjectId;
    private List<StudentMarkDTO> marks;
}