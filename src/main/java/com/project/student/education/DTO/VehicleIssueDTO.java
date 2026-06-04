package com.project.student.education.DTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleIssueDTO {

    private String issueId;

    private String driverId;

    private String issueType;

    private String description;

    private String vehicleNumber;

    private String routeName;

    private String status;

    private LocalDate reportDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}