//package com.project.student.education.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import lombok.Data;
//
//@Entity
//@Data
//public class TransportRoute {
//
//    @Id
//    private String routeId;
//
//    private String routeName;
//
//    private String pickupStartTime;
//    private String dropStartTime;
//
//    private String vehicleName;
//    private String vehicleNumber;
//
//    private String driverName;
//    private String driverPhone;
//}

package com.project.student.education.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transport_routes")
public class TransportRoute {

    @Id
    @Column(name = "route_id")
    private String routeId;

    private String routeName;


    private String pickupStartTime;


    private String dropStartTime;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_id", referencedColumnName = "bus_id")
    private Bus bus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", referencedColumnName = "driver_id")
    private Driver driver;
}
