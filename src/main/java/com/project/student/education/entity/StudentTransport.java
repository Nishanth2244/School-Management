package com.project.student.education.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StudentTransport {

    @Id
    private String id;

    private String studentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "route_id",
            referencedColumnName = "route_id"   )
    private TransportRoute route;

    private String pickupStop;
    private String dropStop;
    private String pickupTime;
    private String dropTime;

    private String feeStatus;




}
