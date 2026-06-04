package com.project.student.education.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "transport_route", schema = "school")
public class TransportRoute {

    @Id
    @Column(name = "route_id")
    private String routeId;

    private String routeName;

    private String pickupStartTime;
    private String dropStartTime;

    private String vehicleName;
    private String vehicleNumber;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIgnore

    private Driver driver;
}
