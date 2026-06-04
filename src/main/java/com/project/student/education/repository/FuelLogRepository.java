package com.project.student.education.repository;

import com.project.student.education.entity.FuelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FuelLogRepository extends JpaRepository<FuelLog,String> {

    List<FuelLog> findByDriverId(String driverId);

    List<FuelLog> findByDriverIdAndFuelDate(
            String driverId,
            LocalDate fuelDate
    );

    List<FuelLog> findByDriverIdAndFuelDateBetween(
            String driverId,
            LocalDate startDate,
            LocalDate endDate
    );
}