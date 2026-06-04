package com.project.student.education.DTO;

import lombok.Data;

@Data
public class RouteResponseDTO {
    private String routeId;
    private String routeName;
    private String pickupStartTime;
    private String dropStartTime;
    private BusResponseDTO bus;       // Nested DTOs instead of full entities
    private DriverResponseDTO driver; // Nested DTOs instead of full entities
}