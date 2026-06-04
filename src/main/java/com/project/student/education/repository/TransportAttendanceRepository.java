package com.project.student.education.repository;

import com.project.student.education.DTO.TransportAttendance;
import com.project.student.education.entity.StudentTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransportAttendanceRepository
        extends JpaRepository<TransportAttendance,String> {

    List<TransportAttendance> findByAttendanceDate(LocalDate date);

    List<TransportAttendance> findByDriverIdAndAttendanceDate(
            String driverId,
            LocalDate date
    );

    Optional<TransportAttendance>
    findByStudentIdAndAttendanceDateAndTripType(
            String studentId,
            LocalDate attendanceDate,
            String tripType
    );
}