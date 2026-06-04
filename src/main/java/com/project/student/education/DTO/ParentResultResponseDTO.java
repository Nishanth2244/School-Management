package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ParentResultResponseDTO {
    private String studentName;
    private Double percentage;
    private Integer rank;
    private List<ParentSubjectMarkDTO> subjects;
}