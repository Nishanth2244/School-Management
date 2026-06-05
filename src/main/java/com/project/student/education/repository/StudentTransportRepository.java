package com.project.student.education.repository;

import com.project.student.education.entity.StudentTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentTransportRepository
        extends JpaRepository<StudentTransport,String> {

    Optional<StudentTransport> findByStudentId(String studentId);

    List<StudentTransport> findByRoute_RouteId(String routeId);

    List<StudentTransport> findByRoute_Driver_Id(String driverId);

    
}