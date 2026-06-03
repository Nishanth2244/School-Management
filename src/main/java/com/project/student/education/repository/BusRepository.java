package com.project.student.education.repository;

import com.project.student.education.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, String> {
    boolean existsByVehicleNumber(String vehicleNumber);
}