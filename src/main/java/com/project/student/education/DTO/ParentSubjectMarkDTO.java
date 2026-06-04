// ParentSubjectMarkDTO.java
package com.project.student.education.DTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParentSubjectMarkDTO {
    private String subjectId;
    private Double obtainedMarks;
    private String remarks;
    private String attendanceStatus;
}