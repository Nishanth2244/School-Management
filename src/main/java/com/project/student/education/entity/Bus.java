package com.project.student.education.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buses")
public class Bus {
    @Id
    @Column(name = "bus_id")
    private String busId;
    private String vehicleName;

    private String vehicleNumber;

    
    private int capacity;
}