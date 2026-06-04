package com.project.student.education.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentExamResponseDTO {
    private String examName;
    private LocalDate startDate;
}