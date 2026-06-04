package com.project.student.education.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleTimetableDTO {
    private String examId;
    private String classSectionId;
    private String subjectId;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
}