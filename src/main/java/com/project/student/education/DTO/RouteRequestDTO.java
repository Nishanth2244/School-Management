package com.project.student.education.DTO;
import lombok.Data;

@Data
public class RouteRequestDTO {
    private String routeName;
    private String pickupStartTime;
    private String dropStartTime;
    private String busId;
    private String driverId;
}