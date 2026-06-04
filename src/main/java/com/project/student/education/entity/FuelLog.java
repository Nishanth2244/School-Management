package com.project.student.education.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class FuelLog {

    @Id
    private String fuelLogId;

    private String driverId;

    private Double litersFilled;

    private Double amount;

    private Double odometerReading;

    private LocalDate fuelDate;
}