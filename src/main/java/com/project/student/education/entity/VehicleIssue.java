package com.project.student.education.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class VehicleIssue {

    @Id
    private String issueId;

    private String driverId;

    private String issueType; // Mechanical Issue, Accident, Fuel Issue, Other

    @Column(length = 2000)
    private String description;

    private String vehicleNumber;

    private String routeName;

    private String status; // PENDING, IN_PROGRESS, RESOLVED

    private LocalDate reportDate;

    private LocalDateTime createdAt;
}