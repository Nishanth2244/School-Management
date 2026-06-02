package com.project.student.education.repository;

import com.project.student.education.entity.TransportRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportRouteRepository extends JpaRepository<TransportRoute, String> {
    boolean existsByRouteName(String routeName);

    List<TransportRoute> findByDriverId(String driverId);
}
