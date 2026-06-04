package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LessonPlanRequestDTO {
    private String classSectionId;
    private String subjectId;
    private String teacherId;
    private String topicName;
    private LocalDate plannedDate;
    private Boolean isCompleted;
}