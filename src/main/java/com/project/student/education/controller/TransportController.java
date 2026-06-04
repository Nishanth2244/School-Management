package com.project.student.education.controller;


import com.project.student.education.DTO.ComprehensiveScheduleRequest;
import com.project.student.education.DTO.StudentTransportDTO;
import com.project.student.education.DTO.TransportAssignRequest;
import com.project.student.education.DTO.TransportRouteRequest;
import com.project.student.education.config.SecurityUtil;
import com.project.student.education.entity.TransportRoute;
import com.project.student.education.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/student/transport")
@RequiredArgsConstructor
public class TransportController {

    private final TransportService transportService;
    private final SecurityUtil  securityUtil;


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
}
