package com.project.student.education.DTO;

import lombok.Data;

@Data

public class StudentTransportDetailsResponse {

    private String studentId;

    private String routeId;

    private String routeName;

    private String driverName;

    private String driverPhone;

    private String pickupTime;

    private String dropTime;
}