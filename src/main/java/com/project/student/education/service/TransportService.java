package com.project.student.education.service;


import com.project.student.education.DTO.*;
import com.project.student.education.entity.*;
import com.project.student.education.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransportService {

    private final IdGenerator idGenerator;
    private final TransportRouteRepository transportRouteRepository;
    private final DriverRepo driverRepo;
    private final TransportAttendanceRepository transportAttendanceRepository;
    private final FuelLogRepository fuelLogRepository;
    private final VehicleIssueRepository vehicleIssueRepository;
    private final DriverIssueRepository driverIssueRepository;
    private final UserRepository userRepository;
    private final ExamMasterRepository examMasterRepository;

    private final StudentTransportRepository studentTransportRepository;
    private final NotificationService notificationService;

    @Transactional
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

    @Transactional
    public StudentTransportDTO assignTransport(String studentId, TransportAssignRequest req) {

        // CHANGE: Explicitly check for route existence before doing anything else
        TransportRoute route = transportRouteRepository.findById(req.getRouteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Assignment rejected: Route ID '" + req.getRouteId() + "' does not exist in the database."
                ));

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
                ? st.getRoute().getDriver().getPhoneNo() : null;

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

    public List<TransportRoute> getRoutesByDriver(String driverId) {
        List<TransportRoute> assignedRoutes = transportRouteRepository.findByDriverId(driverId);

        if (assignedRoutes.isEmpty()) {
            throw new RuntimeException("No routes assigned to this driver yet.");
        }

        return assignedRoutes;
    }

    public List<DriverStudentDTO> getDriverStudents(String driverId) {

        List<StudentTransport> students =
                studentTransportRepository
                        .findByRoute_Driver_Id(driverId);

        return students.stream()
                .map(st -> {

                    String status = "PENDING";

                    Optional<TransportAttendance> attendance =
                            transportAttendanceRepository
                                    .findByStudentIdAndAttendanceDateAndTripType(
                                            st.getStudentId(),
                                            LocalDate.now(),
                                            "PICKUP"
                                    );

                    if (attendance.isPresent()) {
                        status = attendance.get().getStatus();
                    }

                    return DriverStudentDTO.builder()
                            .studentId(st.getStudentId())
                            .pickupStop(st.getPickupStop())
                            .routeName(st.getRoute().getRouteName())
                            .status(status)
                            .build();
                })
                .toList();
    }

    public String markAttendance(
            String driverId,
            AttendanceDriver request) {

        TransportAttendance attendance =
                transportAttendanceRepository
                        .findByStudentIdAndAttendanceDateAndTripType(
                                request.getStudentId(),
                                LocalDate.now(),
                                request.getTripType()
                        )
                        .orElse(new TransportAttendance());

        if (attendance.getAttendanceId() == null) {
            attendance.setAttendanceId(
                    idGenerator.generateId("TAT"));
        }

        attendance.setStudentId(request.getStudentId());
        attendance.setDriverId(driverId);
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setTripType(request.getTripType());
        attendance.setStatus(request.getStatus());

        transportAttendanceRepository.save(attendance);

        return "Attendance marked successfully";
    }

    public AttendanceSummaryDTO getSummary(
            String driverId,
            LocalDate date) {

        List<TransportAttendance> attendance =
                transportAttendanceRepository
                        .findByDriverIdAndAttendanceDate(
                                driverId,
                                date
                        );

        long present =
                attendance.stream()
                        .filter(a -> "PRESENT"
                                .equals(a.getStatus()))
                        .count();

        long absent =
                attendance.stream()
                        .filter(a -> "ABSENT"
                                .equals(a.getStatus()))
                        .count();

        return AttendanceSummaryDTO.builder()
                .present(present)
                .absent(absent)
                .total(attendance.size())
                .build();
    }

    public DriverProfileDTO getDriverProfile(String driverId) {

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        TransportRoute route = transportRouteRepository
                .findByDriver_Id(driverId)
                .stream()
                .findFirst()
                .orElse(null);

        return DriverProfileDTO.builder()
                .driverId(driver.getId())
                .fullName(driver.getFullName())
                .phoneNo(driver.getPhoneNo())

                .busNumber(route != null ? route.getVehicleNumber() : null)
                .vehicleName(route != null ? route.getVehicleName() : null)

                .routeId(route != null ? route.getRouteId() : null)
                .routeName(route != null ? route.getRouteName() : null)

//                .licenceNumber(driver.getLicenceNumber())
//                .licenceStartDate(driver.getLicenceStartDate())
//                .licenceEndDate(driver.getLicenceEndDate())
                .build();
    }

    public FuelLog saveFuelLog(
            String driverId,
            FuelLogRequest request) {

        FuelLog fuelLog = new FuelLog();

        fuelLog.setFuelLogId(
                idGenerator.generateId("FUL")
        );

        fuelLog.setDriverId(driverId);
        fuelLog.setLitersFilled(request.getLitersFilled());
        fuelLog.setAmount(request.getAmount());
        fuelLog.setOdometerReading(request.getOdometerReading());

        fuelLog.setFuelDate(LocalDate.now());

        return fuelLogRepository.save(fuelLog);
    }

    public List<FuelLog> getFuelByDate(
            String driverId,
            LocalDate date) {

        return fuelLogRepository
                .findByDriverIdAndFuelDate(driverId, date);
    }

    public List<FuelLog> getFuelByMonth(
            String driverId,
            int year,
            int month) {

        LocalDate start =
                LocalDate.of(year, month, 1);

        LocalDate end =
                start.withDayOfMonth(
                        start.lengthOfMonth()
                );

        return fuelLogRepository
                .findByDriverIdAndFuelDateBetween(
                        driverId,
                        start,
                        end
                );
    }

    public List<FuelLog> getAllFuel(String driverId) {
        return fuelLogRepository.findByDriverId(driverId);
    }

    public VehicleIssue createIssue(
            String driverId,
            VehicleIssueRequest request) {

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        VehicleIssue issue = new VehicleIssue();

        issue.setIssueId(idGenerator.generateId("VIS"));
        issue.setDriverId(driverId);
        issue.setIssueType(request.getIssueType());
        issue.setDescription(request.getDescription());

        issue.setVehicleNumber(driver.getBusNumber());
        issue.setRouteName(driver.getAssignedRoute());

        issue.setStatus("PENDING");
        issue.setReportDate(LocalDate.now());
        issue.setCreatedAt(LocalDateTime.now());

        return vehicleIssueRepository.save(issue);
    }

    public List<VehicleIssue> getDriverIssues(String driverId) {
        return vehicleIssueRepository.findByDriverId(driverId);
    }

    public List<VehicleIssue> getIssuesByDate(LocalDate date) {
        return vehicleIssueRepository.findByReportDate(date);
    }

    public List<VehicleIssue> getIssuesByMonth(
            int year,
            int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return vehicleIssueRepository.findByReportDateBetween(start, end);
    }

    public VehicleIssue updateStatus(String issueId, String status) {

        VehicleIssue issue = vehicleIssueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));

        List<String> validStatuses = List.of(
                "PENDING",
                "IN_PROGRESS",
                "RESOLVED"
        );

        if (!validStatuses.contains(status.toUpperCase())) {
            throw new RuntimeException(
                    "Invalid status. Allowed values: PENDING, IN_PROGRESS, RESOLVED"
            );
        }

        issue.setStatus(status.toUpperCase());

        return vehicleIssueRepository.save(issue);
    }

    public List<VehicleIssueDTO> getAllIssues() {

        return vehicleIssueRepository.findAll()
                .stream()
                .map(issue -> VehicleIssueDTO.builder()
                        .issueId(issue.getIssueId())
                        .driverId(issue.getDriverId())
                        .issueType(issue.getIssueType())
                        .description(issue.getDescription())
                        .vehicleNumber(issue.getVehicleNumber())
                        .routeName(issue.getRouteName())
                        .status(issue.getStatus())
                        .reportDate(issue.getReportDate())
                        .createdAt(issue.getCreatedAt())
                        .build())
                .toList();
    }

    @Transactional
    public DriverResponseDTO updateDriver(
            String driverId,
            DriverResponseDTO request) {

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Driver not found"));

        driver.setFullName(request.getFullName());
        driver.setExperience(request.getExperience());
        driver.setAddress(request.getAddress());
        driver.setLicenseNo(request.getLicenseNo());
        driver.setPhoneNo(request.getPhoneNo());

        Driver updatedDriver = driverRepo.save(driver);

        DriverResponseDTO response = new DriverResponseDTO();
        response.setId(updatedDriver.getId());
        response.setFullName(updatedDriver.getFullName());
        response.setExperience(updatedDriver.getExperience());
        response.setAddress(updatedDriver.getAddress());
        response.setLicenseNo(updatedDriver.getLicenseNo());
        response.setPhoneNo(updatedDriver.getPhoneNo());

        return response;
    }

    @Transactional
    public String deleteDriver(String driverId) {

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Driver not found"));

        // Delete linked user if present
        if (driver.getUser() != null) {
            userRepository.delete(driver.getUser());
        }

        driverRepo.delete(driver);

        return "Driver deleted successfully";
    }

    public List<StudentTransportDTO> getStudentsByRouteForDriver(String userId, String routeId) {
        // 1. Verify that the route exists
        TransportRoute route = transportRouteRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        // 2. Check if the current user has administrative bypass privileges
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN") ||
                        role.equals("ROLE_SUPER_ADMIN") ||
                        role.equals("ROLE_PRINCIPAL"));

        // 3. If NOT an admin, enforce strict driver-route ownership validation
        if (!isAdmin) {
            if (route.getDriver() == null || !route.getDriver().getId().equals(userId)) {
                throw new RuntimeException("Access Denied: You are not assigned to this route.");
            }
        }

        // 4. Fetch students mapped to this route
        List<StudentTransport> assignments = studentTransportRepository.findByRoute_RouteId(routeId);

        if (assignments.isEmpty()) {
            throw new RuntimeException("No students assigned to this route.");
        }

        // 5. Reuse your existing mapping function
        return assignments.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public List<StudentTransportDTO> assignBulkStudentsToRoute(BatchRouteAssignRequest req) {
        boolean routeExists = transportRouteRepository.existsById(req.getRouteId());
        if (!routeExists) {
            throw new IllegalArgumentException(
                    "Assignment failed: Route ID '" + req.getRouteId() + "' does not exist in the database."
            );
        }

        // 2. Safely fetch the route now that we know it's there
        TransportRoute route = getRoute(req.getRouteId());
        List<StudentTransportDTO> savedDTOs = new ArrayList<>();

        if (req.getStudents() == null || req.getStudents().isEmpty()) {
            throw new RuntimeException("Student list cannot be empty");
        }

        // 3. Loop through and map each student
        for (BatchRouteAssignRequest.StudentAssignmentDetails studentDetail : req.getStudents()) {
            String studentId = studentDetail.getStudentId();

            // Find existing assignment or initialize a new record
            StudentTransport st = studentTransportRepository
                    .findByStudentId(studentId)
                    .orElseGet(() -> {
                        StudentTransport newSt = new StudentTransport();
                        newSt.setId(idGenerator.generateId("STT"));
                        newSt.setStudentId(studentId);
                        return newSt;
                    });

            // Set properties safely
            st.setRoute(route);
            st.setPickupStop(studentDetail.getPickupStop());
            st.setDropStop(studentDetail.getDropStop());
            st.setPickupTime(studentDetail.getPickupTime());
            st.setDropTime(studentDetail.getDropTime());
            st.setFeeStatus(studentDetail.getFeeStatus());

            // Save records
            studentTransportRepository.save(st);

            // Trigger notification service safely
            notificationService.sendNotification(
                    studentId,
                    "transport assigned",
                    "Admin has updated your transport route to: " + route.getRouteName(),
                    "transport"
            );

            savedDTOs.add(mapToDTO(st));
        }

        return savedDTOs;
    }

}