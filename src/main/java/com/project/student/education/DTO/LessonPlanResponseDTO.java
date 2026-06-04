package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LessonPlanResponseDTO {
    private String lessonPlanId;
    private String classSectionId;
    private String className;
    private String section;
    private String subjectName;
    private String teacherName;
    private String topicName;
    private LocalDate plannedDate;
    private Boolean isCompleted;
}