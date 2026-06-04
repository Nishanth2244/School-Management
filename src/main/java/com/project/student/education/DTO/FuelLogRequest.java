package com.project.student.education.DTO;

import lombok.Data;

@Data
public class FuelLogRequest {

    private Double litersFilled;

    private Double amount;

    private Double odometerReading;
}