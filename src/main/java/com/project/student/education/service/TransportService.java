package com.project.student.education.service;


import com.project.student.education.DTO.StudentTransportDTO;
import com.project.student.education.DTO.TransportAssignRequest;
import com.project.student.education.DTO.TransportRouteRequest;
import com.project.student.education.entity.Driver;
import com.project.student.education.entity.IdGenerator;
import com.project.student.education.entity.StudentTransport;
import com.project.student.education.entity.TransportRoute;
import com.project.student.education.repository.DriverRepo;
import com.project.student.education.repository.StudentTransportRepository;
import com.project.student.education.repository.TransportRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor



public class TransportService {

    private final IdGenerator idGenerator;
    private final TransportRouteRepository transportRouteRepository;
    private final DriverRepo driverRepo;

    private final StudentTransportRepository studentTransportRepository;
    private final NotificationService notificationService;
    public TransportRoute createRoute(TransportRouteRequest req) {
        if (transportRouteRepository.existsByRouteName(req.getRouteName())) {
            throw new RuntimeException("Route with same name already exists");
        }

        // 1. Make driver optional
//        Driver driver = null;
//        if (req.getDriverId() != null && !req.getDriverId().isBlank()) {
//            driver = driverRepo.findById(req.getDriverId())
//                    .orElseThrow(() -> new RuntimeException("Driver not found"));
//        }

        TransportRoute route = new TransportRoute();
        route.setRouteId(idGenerator.generateId("TRT"));
        route.setRouteName(req.getRouteName());
        route.setPickupStartTime(req.getPickupStartTime());
        route.setDropStartTime(req.getDropStartTime());
        route.setVehicleName(req.getVehicleName());
        route.setVehicleNumber(req.getVehicleNumber());
//        route.setDriver(driver); // Will be null if no ID was passed

        return transportRouteRepository.save(route);
    }

    public TransportRoute updateRoute(String routeId, TransportRouteRequest updated) {
        TransportRoute existing = getRoute(routeId);

//        Driver driver = null;
//        if (updated.getDriverId() != null && !updated.getDriverId().isBlank()) {
//            driver = driverRepo.findById(updated.getDriverId())
//                    .orElseThrow(() -> new RuntimeException("Driver not found"));
//        }

        existing.setRouteName(updated.getRouteName());
        existing.setPickupStartTime(updated.getPickupStartTime());
        existing.setDropStartTime(updated.getDropStartTime());
        existing.setVehicleName(updated.getVehicleName());
        existing.setVehicleNumber(updated.getVehicleNumber());
//        existing.setDriver(driver);

        return transportRouteRepository.save(existing);
    }

    public TransportRoute getRoute(String routeId) {
        return transportRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
    }

    public StudentTransportDTO assignTransport(String studentId, TransportAssignRequest req) {

        TransportRoute route = getRoute(req.getRouteId());

        StudentTransport st = studentTransportRepository
                .findByStudentId(studentId)
                .orElseGet(() -> {
                    StudentTransport newSt = new StudentTransport();
                    newSt.setId(idGenerator.generateId("STT"));
                    newSt.setStudentId(studentId);
                    return newSt;
                });

        st.setRoute(route);
        st.setPickupStop(req.getPickupStop());
        st.setDropStop(req.getDropStop());
        st.setPickupTime(req.getPickupTime());
        st.setDropTime(req.getDropTime());
        st.setFeeStatus(req.getFeeStatus());

        studentTransportRepository.save(st);
        notificationService.sendNotification(
                studentId,
                "transport assigned",
                "Message",

                "transport"
        );

        return mapToDTO(st);
    }




    public StudentTransportDTO getStudentTransportDetails(String studentId) {

        StudentTransport st = studentTransportRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Transport not assigned"));

        return mapToDTO(st);
    }

    public List<StudentTransportDTO> getStudentsByRoute(String routeId) {

        List<StudentTransport> assignments =
                studentTransportRepository.findByRoute_RouteId(routeId);

        if (assignments.isEmpty()) {
            throw new RuntimeException("No students assigned to this route");
        }

        return assignments.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<TransportRoute> getAllRoute() {
        return transportRouteRepository.findAll();
    }
    private StudentTransportDTO mapToDTO(StudentTransport st) {

        String dName = (st.getRoute() != null && st.getRoute().getDriver() != null)
                ? st.getRoute().getDriver().getFullName() : null;
        String dPhone = (st.getRoute() != null && st.getRoute().getDriver() != null)
                ? st.getRoute().getDriver().getPhone() : null;

        return StudentTransportDTO.builder()
                .studentId(st.getStudentId())
                .routeName(st.getRoute() != null ? st.getRoute().getRouteName() : null)
                .pickupStop(st.getPickupStop())
                .dropStop(st.getDropStop())
                .pickupTime(st.getPickupTime())
                .dropTime(st.getDropTime())
                .vehicleName(st.getRoute() != null ? st.getRoute().getVehicleName() : null)
                .vehicleNumber(st.getRoute() != null ? st.getRoute().getVehicleNumber() : null)
                .driverName(dName)
                .driverPhone(dPhone)
                .feeStatus(st.getFeeStatus())
                .build();
    }
    public TransportRoute assignDriverToRoute(String routeId, String driverId) {
        TransportRoute route = getRoute(routeId);

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        route.setDriver(driver); // Attach the driver

        return transportRouteRepository.save(route);
    }
}