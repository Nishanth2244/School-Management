//package com.project.student.education.service;
//
//
//import com.project.student.education.DTO.StudentTransportDTO;
//import com.project.student.education.DTO.TransportAssignRequest;
//import com.project.student.education.entity.IdGenerator;
//import com.project.student.education.entity.StudentTransport;
//import com.project.student.education.entity.TransportRoute;
//import com.project.student.education.repository.StudentTransportRepository;
//import com.project.student.education.repository.TransportRouteRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//
//
//
//public class TransportService {
//
//    private final IdGenerator idGenerator;
//    private final TransportRouteRepository transportRouteRepository;
//
//    private final StudentTransportRepository studentTransportRepository;
//    private final NotificationService notificationService;
//
//    public TransportRoute createRoute(TransportRoute route) {
//        if (transportRouteRepository.existsByRouteName(route.getRouteName())) {
//            throw new RuntimeException("Route with same name already exists");
//        }
//        route.setRouteId(idGenerator.generateId("TRT"));
//        return transportRouteRepository.save(route);
//    }
//
//    public TransportRoute updateRoute(String routeId, TransportRoute updated) {
//        TransportRoute existing = getRoute(routeId);
//
//        existing.setRouteName(updated.getRouteName());
//        existing.setPickupStartTime(updated.getPickupStartTime());
//        existing.setDropStartTime(updated.getDropStartTime());
//        existing.setVehicleName(updated.getVehicleName());
//        existing.setVehicleNumber(updated.getVehicleNumber());
//        existing.setDriverName(updated.getDriverName());
//        existing.setDriverPhone(updated.getDriverPhone());
//
//        return transportRouteRepository.save(existing);
//    }
//
//    public TransportRoute getRoute(String routeId) {
//        return transportRouteRepository.findById(routeId)
//                .orElseThrow(() -> new RuntimeException("Route not found"));
//    }
//
//    public StudentTransportDTO assignTransport(String studentId, TransportAssignRequest req) {
//
//        TransportRoute route = getRoute(req.getRouteId());
//
//        StudentTransport st = studentTransportRepository
//                .findByStudentId(studentId)
//                .orElseGet(() -> {
//                    StudentTransport newSt = new StudentTransport();
//                    newSt.setId(idGenerator.generateId("STT"));
//                    newSt.setStudentId(studentId);
//                    return newSt;
//                });
//
//        st.setRoute(route);
//        st.setPickupStop(req.getPickupStop());
//        st.setDropStop(req.getDropStop());
//        st.setPickupTime(req.getPickupTime());
//        st.setDropTime(req.getDropTime());
//        st.setFeeStatus(req.getFeeStatus());
//
//        studentTransportRepository.save(st);
//        notificationService.sendNotification(
//                studentId,
//                "transport assigned",
//                "Message",
//
//                "transport"
//        );
//
//        return mapToDTO(st);
//    }
//
//
//
//
//    public StudentTransportDTO getStudentTransportDetails(String studentId) {
//
//        StudentTransport st = studentTransportRepository.findByStudentId(studentId)
//                .orElseThrow(() -> new RuntimeException("Transport not assigned"));
//
//        return mapToDTO(st);
//    }
//
//    public List<StudentTransportDTO> getStudentsByRoute(String routeId) {
//
//        List<StudentTransport> assignments =
//                studentTransportRepository.findByRoute_RouteId(routeId);
//
//        if (assignments.isEmpty()) {
//            throw new RuntimeException("No students assigned to this route");
//        }
//
//        List<StudentTransportDTO> result = new ArrayList<>();
//
//        for (StudentTransport assign : assignments) {
//
//            StudentTransportDTO dto = new StudentTransportDTO();
//
//
//            dto.setStudentId(assign.getStudentId());
//
//
//            dto.setRouteName(assign.getRoute().getRouteName());
//            dto.setPickupStop(assign.getPickupStop());
//            dto.setDropStop(assign.getDropStop());
//
//            dto.setPickupTime(assign.getPickupTime());
//            dto.setDropTime(assign.getDropTime());
//
//            if (assign.getRoute() != null) {
//                dto.setVehicleName(assign.getRoute().getVehicleName());
//                dto.setVehicleNumber(assign.getRoute().getVehicleNumber());
//                dto.setDriverName(assign.getRoute().getDriverName());
//                dto.setDriverPhone(assign.getRoute().getDriverPhone());
//            }
//
//            dto.setFeeStatus(assign.getFeeStatus());
//
//            result.add(dto);
//        }
//
//        return result;
//    }
//
//    public List<TransportRoute> getAllRoute() {
//        return transportRouteRepository.findAll();
//    }
//    private StudentTransportDTO mapToDTO(StudentTransport st) {
//
//        return StudentTransportDTO.builder()
//                .studentId(st.getStudentId())
//                .routeName(st.getRoute().getRouteName())
//                .pickupStop(st.getPickupStop())
//                .dropStop(st.getDropStop())
//                .pickupTime(st.getPickupTime())
//                .dropTime(st.getDropTime())
//                .vehicleName(st.getRoute().getVehicleName())
//                .vehicleNumber(st.getRoute().getVehicleNumber())
//                .driverName(st.getRoute().getDriverName())
//                .driverPhone(st.getRoute().getDriverPhone())
//                .feeStatus(st.getFeeStatus())
//                .build();
//    }
//}

package com.project.student.education.service;

import com.project.student.education.DTO.*;
import com.project.student.education.entity.*;
import com.project.student.education.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportService {

    private final IdGenerator idGenerator;
    private final BusRepository busRepository;
    private final DriverRepository driverRepository;
    private final TransportRouteRepository routeRepository;

    // ==========================================
    // BUS CRUD OPERATIONS
    // ==========================================
    public BusResponseDTO createBus(BusRequestDTO dto) {
        if (busRepository.existsByVehicleNumber(dto.getVehicleNumber())) {
            throw new RuntimeException("Bus with this vehicle number already exists");
        }
        Bus bus = Bus.builder()
                .busId(idGenerator.generateId("BUS"))
                .vehicleName(dto.getVehicleName())
                .vehicleNumber(dto.getVehicleNumber())
                .capacity(dto.getCapacity())
                .build();
        return mapToBusResponse(busRepository.save(bus));
    }

    public List<BusResponseDTO> getAllBuses() {
        return busRepository.findAll().stream()
                .map(this::mapToBusResponse)
                .collect(Collectors.toList());
    }

    public BusResponseDTO getBusById(String id) {
        Bus bus = busRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
        return mapToBusResponse(bus);
    }

    public BusResponseDTO updateBus(String id, BusRequestDTO dto) {
        Bus existing = busRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
        existing.setVehicleName(dto.getVehicleName());
        existing.setVehicleNumber(dto.getVehicleNumber());
        existing.setCapacity(dto.getCapacity());
        return mapToBusResponse(busRepository.save(existing));
    }

    public void deleteBus(String id) {
        if (!busRepository.existsById(id)) throw new RuntimeException("Bus not found");
        busRepository.deleteById(id);
    }

    // ==========================================
    // DRIVER CRUD OPERATIONS
    // ==========================================
    public DriverResponseDTO createDriver(DriverRequestDTO dto) {
        if (driverRepository.existsByPhone(dto.getPhone())) {
            throw new RuntimeException("Driver with this phone number already registered");
        }
        Driver driver = Driver.builder()
                .driverId(idGenerator.generateId("DRV"))
                .name(dto.getName())
                .phone(dto.getPhone())
                .licenseNumber(dto.getLicenseNumber())
                .build();
        return mapToDriverResponse(driverRepository.save(driver));
    }

    public List<DriverResponseDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::mapToDriverResponse)
                .collect(Collectors.toList());
    }

    public DriverResponseDTO getDriverById(String id) {
        Driver driver = driverRepository.findById(id).orElseThrow(() -> new RuntimeException("Driver not found"));
        return mapToDriverResponse(driver);
    }

    public DriverResponseDTO updateDriver(String id, DriverRequestDTO dto) {
        Driver existing = driverRepository.findById(id).orElseThrow(() -> new RuntimeException("Driver not found"));
        existing.setName(dto.getName());
        existing.setPhone(dto.getPhone());
        existing.setLicenseNumber(dto.getLicenseNumber());
        return mapToDriverResponse(driverRepository.save(existing));
    }

    public void deleteDriver(String id) {
        if (!driverRepository.existsById(id)) throw new RuntimeException("Driver not found");
        driverRepository.deleteById(id);
    }

    // ==========================================
    // ROUTE CRUD OPERATIONS
    // ==========================================
    public RouteResponseDTO createRoute(RouteRequestDTO dto) {
        if (routeRepository.existsByRouteName(dto.getRouteName())) {
            throw new RuntimeException("Route name already exists");
        }

        Bus bus = dto.getBusId() != null ? busRepository.findById(dto.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus assigned to route not found")) : null;

        Driver driver = dto.getDriverId() != null ? driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver assigned to route not found")) : null;

        TransportRoute route = TransportRoute.builder()
                .routeId(idGenerator.generateId("RTE"))
                .routeName(dto.getRouteName())
                .pickupStartTime(dto.getPickupStartTime())
                .dropStartTime(dto.getDropStartTime())
                .bus(bus)
                .driver(driver)
                .build();

        return mapToRouteResponse(routeRepository.save(route));
    }

    public List<RouteResponseDTO> getAllRoutes() {
        return routeRepository.findAll().stream()
                .map(this::mapToRouteResponse)
                .collect(Collectors.toList());
    }

    public RouteResponseDTO getRouteById(String id) {
        TransportRoute route = routeRepository.findById(id).orElseThrow(() -> new RuntimeException("Route not found"));
        return mapToRouteResponse(route);
    }

    public RouteResponseDTO updateRoute(String id, RouteRequestDTO dto) {
        TransportRoute existing = routeRepository.findById(id).orElseThrow(() -> new RuntimeException("Route not found"));

        Bus bus = dto.getBusId() != null ? busRepository.findById(dto.getBusId())
                .orElseThrow(() -> new RuntimeException("Bus not found")) : null;

        Driver driver = dto.getDriverId() != null ? driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found")) : null;

        existing.setRouteName(dto.getRouteName());
        existing.setPickupStartTime(dto.getPickupStartTime());
        existing.setDropStartTime(dto.getDropStartTime());
        existing.setBus(bus);
        existing.setDriver(driver);

        return mapToRouteResponse(routeRepository.save(existing));
    }

    public void deleteRoute(String id) {
        if (!routeRepository.existsById(id)) throw new RuntimeException("Route not found");
        routeRepository.deleteById(id);
    }

    // ==========================================
    // MAPPER FUNCTIONS (Entity -> DTO conversion)
    // ==========================================
    private BusResponseDTO mapToBusResponse(Bus bus) {
        if (bus == null) return null;
        BusResponseDTO dto = new BusResponseDTO();
        dto.setBusId(bus.getBusId());
        dto.setVehicleName(bus.getVehicleName());
        dto.setVehicleNumber(bus.getVehicleNumber());
        dto.setCapacity(bus.getCapacity());
        return dto;
    }

    private DriverResponseDTO mapToDriverResponse(Driver driver) {
        if (driver == null) return null;
        DriverResponseDTO dto = new DriverResponseDTO();
        dto.setDriverId(driver.getDriverId());
        dto.setName(driver.getName());
        dto.setPhone(driver.getPhone());
        dto.setLicenseNumber(driver.getLicenseNumber());
        return dto;
    }

    private RouteResponseDTO mapToRouteResponse(TransportRoute route) {
        if (route == null) return null;
        RouteResponseDTO dto = new RouteResponseDTO();
        dto.setRouteId(route.getRouteId());
        dto.setRouteName(route.getRouteName());
        dto.setPickupStartTime(route.getPickupStartTime());
        dto.setDropStartTime(route.getDropStartTime());
        dto.setBus(mapToBusResponse(route.getBus()));
        dto.setDriver(mapToDriverResponse(route.getDriver()));
        return dto;
    }
}