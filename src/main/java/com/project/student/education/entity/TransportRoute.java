package com.project.student.education.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class TransportRoute {

    @Id
    private String routeId;

    private String routeName;

    private String pickupStartTime;
    private String dropStartTime;

    private String vehicleName;
    private String vehicleNumber;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
