package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DriverProfileDTO {

    private String driverId;
    private String fullName;
    private String phoneNo;

    private String busNumber;
    private String vehicleName;

    private String routeId;
    private String routeName;

    private String licenceNumber;
    private LocalDate licenceStartDate;
    private LocalDate licenceEndDate;
}