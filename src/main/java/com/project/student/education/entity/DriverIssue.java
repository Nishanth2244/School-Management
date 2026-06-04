package com.project.student.education.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_issues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverIssue {

    @Id
    private String issueId;

    private String driverId;

    private String driverName;

    private String title;

    @Column(length = 5000)
    private String description;

    /**
     * OPEN
     * IN_PROGRESS
     * RESOLVED
     * CLOSED
     */
    private String status;

    @Column(length = 2000)
    private String adminRemarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;
}