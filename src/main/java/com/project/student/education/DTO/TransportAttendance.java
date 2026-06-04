package com.project.student.education.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportAttendance {

    @Id
    private String attendanceId;

    private String studentId;

    private String driverId;

    private LocalDate attendanceDate;

    private String status;

    private String tripType; 
}