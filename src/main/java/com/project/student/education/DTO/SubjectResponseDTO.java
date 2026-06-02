package com.project.student.education.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectResponseDTO {
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private Boolean active;
}
