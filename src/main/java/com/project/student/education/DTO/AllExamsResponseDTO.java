package com.project.student.education.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllExamsResponseDTO {
    private String examId;
    private String examName;
    private String academicYear;
    private String term; // e.g., "Term 1", "Finals"
    private List<String> assignedClassSectionIds; // List of class sections tagged to this exam
}