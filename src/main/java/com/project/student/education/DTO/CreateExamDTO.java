package com.project.student.education.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateExamDTO {
    private String examName;
    private String academicYear;
    private LocalDate startDate;
    private LocalDate endDate;

}