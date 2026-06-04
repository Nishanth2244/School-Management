package com.project.student.education.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverIssueDTO {

    private String issueId;

    private String driverId;

    private String driverName;

    private String title;

    private String description;

    private String status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED

    private String adminRemarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}