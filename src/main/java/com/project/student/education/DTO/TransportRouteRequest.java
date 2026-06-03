package com.project.student.education.DTO;

import lombok.Data;

@Data
public class TransportRouteRequest {
    private String routeName;
    private String pickupStartTime;
    private String dropStartTime;
    private String vehicleName;
    private String vehicleNumber;
//    private String driverId; // Frontend will pass the ID here
}