package com.project.student.education.DTO;

import lombok.Data;

@Data
public class BusResponseDTO {
    private String busId;
    private String vehicleName;
    private String vehicleNumber;
    private int capacity;
}