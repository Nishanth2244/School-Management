package com.project.student.education.controller;

import com.project.student.education.DTO.*;

import com.project.student.education.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/student/transport-management")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL')")
public class TransportController {

    private final TransportService managementService;

    @PostMapping("/buses")
    public ResponseEntity<BusResponseDTO> createBus(@RequestBody BusRequestDTO busRequest) {
        return ResponseEntity.ok(managementService.createBus(busRequest));
    }

    @GetMapping("/buses")
    public ResponseEntity<List<BusResponseDTO>> getAllBuses() {
        return ResponseEntity.ok(managementService.getAllBuses());
    }

    @GetMapping("/buses/{id}")
    public ResponseEntity<BusResponseDTO> getBusById(@PathVariable String id) {
        return ResponseEntity.ok(managementService.getBusById(id));
    }

    @PutMapping("/buses/{id}")
    public ResponseEntity<BusResponseDTO> updateBus(@PathVariable String id, @RequestBody BusRequestDTO busRequest) {
        return ResponseEntity.ok(managementService.updateBus(id, busRequest));
    }

    @DeleteMapping("/buses/{id}")
    public ResponseEntity<String> deleteBus(@PathVariable String id) {
        managementService.deleteBus(id);
        return ResponseEntity.ok("Bus asset deleted successfully");
    }

    // --- DRIVER CRUD ---
    @PostMapping("/drivers")
    public ResponseEntity<DriverResponseDTO> createDriver(@RequestBody DriverRequestDTO driverRequest) {
        return ResponseEntity.ok(managementService.createDriver(driverRequest));
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<DriverResponseDTO>> getAllDrivers() {
        return ResponseEntity.ok(managementService.getAllDrivers());
    }

    @GetMapping("/drivers/{id}")
    public ResponseEntity<DriverResponseDTO> getDriverById(@PathVariable String id) {
        return ResponseEntity.ok(managementService.getDriverById(id));
    }

    @PutMapping("/drivers/{id}")
    public ResponseEntity<DriverResponseDTO> updateDriver(@PathVariable String id, @RequestBody DriverRequestDTO driverRequest) {
        return ResponseEntity.ok(managementService.updateDriver(id, driverRequest));
    }

    @DeleteMapping("/drivers/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable String id) {
        managementService.deleteDriver(id);
        return ResponseEntity.ok("Driver asset profile deleted successfully");
    }

    // --- ROUTE CRUD ---
    @PostMapping("/routes")
    public ResponseEntity<RouteResponseDTO> createRoute(@RequestBody RouteRequestDTO routeRequest) {
        return ResponseEntity.ok(managementService.createRoute(routeRequest));
    }

    @GetMapping("/routes")
    public ResponseEntity<List<RouteResponseDTO>> getAllRoutes() {
        return ResponseEntity.ok(managementService.getAllRoutes());
    }

    @GetMapping("/routes/{id}")
    public ResponseEntity<RouteResponseDTO> getRouteById(@PathVariable String id) {
        return ResponseEntity.ok(managementService.getRouteById(id));
    }

    @PutMapping("/routes/{id}")
    public ResponseEntity<RouteResponseDTO> updateRoute(@PathVariable String id, @RequestBody RouteRequestDTO routeRequest) {
        return ResponseEntity.ok(managementService.updateRoute(id, routeRequest));
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<String> deleteRoute(@PathVariable String id) {
        managementService.deleteRoute(id);
        return ResponseEntity.ok("Route asset configuration deleted successfully");
    }
}