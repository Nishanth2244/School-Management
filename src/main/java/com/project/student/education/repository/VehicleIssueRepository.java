package com.project.student.education.repository;

import com.project.student.education.entity.VehicleIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleIssueRepository extends JpaRepository<VehicleIssue,String> {

    List<VehicleIssue> findByDriverId(String driverId);

    List<VehicleIssue> findByReportDate(LocalDate date);

    List<VehicleIssue> findByReportDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}