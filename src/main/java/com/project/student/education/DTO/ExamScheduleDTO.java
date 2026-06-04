package com.project.student.education.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamScheduleDTO {
    private String scheduleId;
    private String examId;
    private String classSectionId;
    private String subjectId;
    private LocalDate examDate;
    private LocalTime startTime;
    private LocalTime endTime;
}