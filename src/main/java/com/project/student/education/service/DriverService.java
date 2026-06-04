package com.project.student.education.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.project.student.education.DTO.DriverResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.student.education.DTO.CompleteDriverOnboardingRequest;
import com.project.student.education.DTO.InviteDriverRequest;
import com.project.student.education.entity.Driver;
import com.project.student.education.entity.DriverInvitation;
import com.project.student.education.entity.IdGenerator;
import com.project.student.education.entity.User;
import com.project.student.education.enums.Role;
import com.project.student.education.repository.DriverInvitationRepo;
import com.project.student.education.repository.DriverRepo;
import com.project.student.education.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {
	
	private final UserRepository userRepository;
	private final DriverInvitationRepo driverInvitationRepo;
	private final EmailService emailService;
	private final IdGenerator idGenerator;
	private final PasswordEncoder passwordEncoder;
	private final DriverRepo driverRepo;

	public String inviteDriver(Long userId, InviteDriverRequest request) {
		
		if (userRepository.existsByEmail(request.getEmail())) {
	        throw new IllegalArgumentException("User with email already exists.");
	    }
		
		User admin = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("Admin not found."));
		
		String secureToken = UUID.randomUUID().toString();
	    System.out.println("DEV ONLY - Driver Token for " + request.getEmail() + " : " + secureToken);
	    
	    DriverInvitation invitation = new DriverInvitation();
	    invitation.setEmail(request.getEmail());
	    invitation.setFullName(request.getFullName());
	    invitation.setToken(secureToken);
	    invitation.setStatus(DriverInvitation.InvitationStatus.PENDING);
	    invitation.setInvitedBy(admin);
	    invitation.setExpiresAt(LocalDateTime.now().plusHours(24));

	    driverInvitationRepo.save(invitation);

	    String inviteLink = "http://localhost:8080/modal?token=" + secureToken + "&role=driver";
	    emailService.sendDriverInviteEmail(request.getEmail(), request.getFullName(), inviteLink);

	    return "Invitation sent successfully to Driver: " + request.getEmail();
	}
	
	
	
	@Transactional
	public String completeDriverOnboarding(CompleteDriverOnboardingRequest request, String token) {
		
		DriverInvitation invitation = driverInvitationRepo.findByToken(token)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid token."));
		
		
		if (invitation.getStatus() != DriverInvitation.InvitationStatus.PENDING || invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
		        throw new IllegalArgumentException("Token is used or expired.");
		    }
		
		String generatedUsername = idGenerator.generateIdWithoutYear("ACS-DRV-");
		
		User driverUser = new User();
		driverUser.setUsername(generatedUsername);
		driverUser.setPassword(passwordEncoder.encode(request.getPassword()));
		driverUser.setFullName(request.getFullName());
		driverUser.setRole(Role.DRIVER);
		driverUser.setEmail(invitation.getEmail());
		driverUser.setApprovalStatus(true);
		driverUser.setIsAvailable(true);
		
		User savedDriver = userRepository.save(driverUser);
		
		invitation.setStatus(DriverInvitation.InvitationStatus.USED);
	    driverInvitationRepo.save(invitation);
	    
	    Driver driver = new Driver();
	    driver.setId(savedDriver.getUsername());
	    driver.setAddress(request.getAddress());
	    driver.setExperience(request.getExperience());
	    driver.setFullName(savedDriver.getFullName());
	    driver.setLicenseNo(request.getLicenseNumber());
	    driver.setPhoneNo(request.getPhoneNo());
		
	    driverRepo.save(driver);

	    return "Driver onboarding complete! ID assigned: " + generatedUsername;
	    
	}

    public List<DriverResponseDTO> getAllDrivers() {

        return driverRepo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    private DriverResponseDTO mapToDto(Driver driver) {

        DriverResponseDTO dto = new DriverResponseDTO();

        dto.setId(driver.getId());
        dto.setFullName(driver.getFullName());
        dto.setExperience(driver.getExperience());
        dto.setAddress(driver.getAddress());
        dto.setLicenseNo(driver.getLicenseNo());
        dto.setPhoneNo(driver.getPhoneNo());

        return dto;
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
}
