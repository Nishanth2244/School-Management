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
public class HallTicketResponseDTO {
    
    private String studentId;
    private String studentName;
    private String rollNumber;
    private String examName;
    private String classSectionName; // Combines Class Name and Section (e.g., "Class 10 - A")
    private String academicYear;
    
    // Nested collection containing the dynamic exam schedule / timetable layout
    private List<ExamScheduleDTO> schedules;
}