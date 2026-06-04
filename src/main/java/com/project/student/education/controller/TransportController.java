package com.project.student.education.controller;

import com.project.student.education.DTO.*;
import com.project.student.education.config.SecurityUtil;
import com.project.student.education.entity.FuelLog;
import com.project.student.education.entity.TransportRoute;
import com.project.student.education.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/student/transport")
@RequiredArgsConstructor
public class TransportController {

    private final TransportService transportService;
    private final SecurityUtil securityUtil;

    // ADMIN ONLY — Create route
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @PostMapping("/route")
    public ResponseEntity<TransportRoute> create(@RequestBody TransportRouteRequest routeReq) {
        return ResponseEntity.ok(transportService.createRoute(routeReq));
    }

    // ADMIN ONLY — Update route
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TransportRoute> update(
            @PathVariable String id,
            @RequestBody TransportRouteRequest routeReq) {
        return ResponseEntity.ok(transportService.updateRoute(id, routeReq));
    }

    // ADMIN ONLY — Assign transport
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @PostMapping("/assign/{studentId}")
    public ResponseEntity<StudentTransportDTO> assign(
            @PathVariable String studentId,
            @RequestBody TransportAssignRequest assignRequest) {

        return ResponseEntity.ok(transportService.assignTransport(studentId, assignRequest));
    }

    // ADMIN ONLY — Update transport
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @PutMapping("/assign/{studentId}")
    public ResponseEntity<StudentTransportDTO> updateTransport(
            @PathVariable String studentId,
            @RequestBody TransportAssignRequest assignRequest) {

        return ResponseEntity.ok(transportService.assignTransport(studentId, assignRequest));
    }

    // ADMIN + STUDENT + PARENT — Student must only access own details
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL','STUDENT','PARENT')")
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentTransportDTO> getDetails(@PathVariable String studentId) {
        return ResponseEntity.ok(transportService.getStudentTransportDetails(studentId));
    }

    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN', 'SUPER_ADMIN', 'PRINCIPAL')")
    @GetMapping("/driver/route/{routeId}/students")
    public ResponseEntity<List<StudentTransportDTO>> getDriverStudentsByRoute(@PathVariable String routeId) {

        // Fetch the current user's username/ID from the security context
        String currentUserId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(transportService.getStudentsByRouteForDriver(currentUserId, routeId));
    }

    // ADMIN + TEACHER — View students using each transport route
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN','TEACHER')")
    @GetMapping("/route/{routeId}/students")
    public ResponseEntity<?> getStudentsByRoute(@PathVariable String routeId) {
        return ResponseEntity.ok(transportService.getStudentsByRoute(routeId));
    }

    // ADMIN + TEACHER — View all transport routes
    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN','TEACHER')")
    @GetMapping("/routes")
    public ResponseEntity<List<TransportRoute>> getRoutes() {
        return ResponseEntity.ok(transportService.getAllRoute());
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN')")
    @PutMapping("/route/{routeId}/assign-driver")
    public ResponseEntity<TransportRoute> assignDriverToRoute(
            @PathVariable String routeId,
            @RequestParam String driverId) {

        return ResponseEntity.ok(transportService.assignDriverToRoute(routeId, driverId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL','SUPER_ADMIN', 'DRIVER')")
    @GetMapping("/driver/my-routes")
    public ResponseEntity<List<TransportRoute>> getMyRoutes() {

        // Securely fetch the driver's ID from the JWT token
        String currentDriverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(transportService.getRoutesByDriver(currentDriverId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN', 'DRIVER')")
    @GetMapping("/students")
    public ResponseEntity<?> getStudents() {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.getDriverStudents(driverId));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN', 'DRIVER')")
    @PostMapping("/attendance")
    public ResponseEntity<?> markAttendance(
            @RequestBody AttendanceDriver request) {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.markAttendance(
                        driverId,
                        request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN', 'DRIVER','PRINCIPAL')")
    @GetMapping("/attendance-summary")
    public ResponseEntity<?> getSummary(
            @RequestParam String date) {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.getSummary(
                        driverId,
                        LocalDate.parse(date)));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/driver/profile")
    public ResponseEntity<DriverProfileDTO> getDriverProfile() {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.getDriverProfile(driverId));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/addfuel")
    public ResponseEntity<FuelLog> addFuel(
            @RequestBody FuelLogRequest request) {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.saveFuelLog(
                        driverId,
                        request));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/getALl")
    public ResponseEntity<List<FuelLog>> getAllFuel() {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.getAllFuel(driverId));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/date")
    public ResponseEntity<List<FuelLog>> getFuelByDate(
            @RequestParam String date) {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.getFuelByDate(
                        driverId,
                        LocalDate.parse(date)));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/month")
    public ResponseEntity<List<FuelLog>> getFuelByMonth(
            @RequestParam int year,
            @RequestParam int month) {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.getFuelByMonth(
                        driverId,
                        year,
                        month));
    }

    @PreAuthorize("hasRole('DRIVER')")
    @PostMapping("/createIssue")
    public ResponseEntity<?> createIssue(
            @RequestBody VehicleIssueRequest request) {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.createIssue(driverId, request));
    }

    // Driver views own issues
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL','DRIVER')")
    @GetMapping("/myIssues")
    public ResponseEntity<?> myIssues() {

        String driverId = securityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                transportService.getDriverIssues(driverId));
    }

    // Admin/Principal view all issues by date
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL','DRIVER')")
    @GetMapping("/date/Issue")
    public ResponseEntity<?> getByDate(
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                transportService.getIssuesByDate(date));
    }

    // Admin/Principal view issues by month
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL','DRIVER')")
    @GetMapping("/month/issue")
    public ResponseEntity<?> getByMonth(
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                transportService.getIssuesByMonth(year, month));
    }

    @PatchMapping("/{issueId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL','DRIVER')")
    public ResponseEntity<?> updateStatus(
            @PathVariable String issueId,
            @RequestParam String status) {

        return ResponseEntity.ok(
                transportService.updateStatus(issueId, status));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL')")
    @GetMapping("/all-issues")
    public ResponseEntity<?> getAllIssues() {

        return ResponseEntity.ok(
                transportService.getAllIssues());
    }

    @PutMapping("/{driverId}")
    public DriverResponseDTO updateDriver(
            @PathVariable String driverId,
            @RequestBody DriverResponseDTO request) {

        return transportService.updateDriver(driverId, request);
    }

    @DeleteMapping("/{driverId}")
    public String deleteDriver(
            @PathVariable String driverId) {

        return transportService.deleteDriver(driverId);
    }

    // @PreAuthorize("hasRole('DRIVER')")
    // @GetMapping("/driver/route/{routeId}/students")
    // public ResponseEntity<List<StudentTransportDTO>>
    // getDriverStudentsByRoute(@PathVariable String routeId) {
    //
    // // Securely fetch the driver's ID from the JWT token
    // String currentDriverId = securityUtil.getCurrentUsername();
    //
    // return
    // ResponseEntity.ok(transportService.getStudentsByRouteForDriver(currentDriverId,
    // routeId));
    // }

    // ADMIN ONLY — Bulk assign multiple students to a route
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','PRINCIPAL')")
    @PostMapping("/route/batch-assign")
    public ResponseEntity<List<StudentTransportDTO>> batchAssignStudents(
            @RequestBody BatchRouteAssignRequest batchRequest) {

        return ResponseEntity.ok(transportService.assignBulkStudentsToRoute(batchRequest));
    }

}
