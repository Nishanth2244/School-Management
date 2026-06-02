package com.project.student.education.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.project.student.education.DTO.AdminCreateRequestDTO;
import com.project.student.education.DTO.CompleteOnboardingRequestDTO;
import com.project.student.education.DTO.InviteAdminRequest;
import com.project.student.education.entity.Admin;
import com.project.student.education.entity.AdminInvitation;
import com.project.student.education.entity.IdGenerator;
import com.project.student.education.entity.User;
import com.project.student.education.enums.Role;
import com.project.student.education.repository.AdminInvitationRepo;
import com.project.student.education.repository.AdminRepo;
import com.project.student.education.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuperAdminService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AdminRepo adminRepo;
	private final AdminInvitationRepo adminInvitationRepo;
	private final EmailService emailService;
	private final IdGenerator idGenerator;

	@Value("${app.frontend.onboarding-url}")
	private String onboardingBaseUrl;


	public void sendInvite(Long userId, InviteAdminRequest inviteAdminRequest) {
		// Validations
		if (userRepository.findByEmail(inviteAdminRequest.getEmail()).isPresent()) {
			throw new IllegalArgumentException(
					"User with email " + inviteAdminRequest.getEmail() + " already exists in the system.");
		}

		User superAdmin = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("SuperAdmin not found. Invalid ID."));

		String secureToken = UUID.randomUUID().toString();

		AdminInvitation invitation = new AdminInvitation();
		invitation.setEmail(inviteAdminRequest.getEmail());
		invitation.setFullName(inviteAdminRequest.getFullName());
		invitation.setToken(secureToken);
		invitation.setIntendedRole(inviteAdminRequest.getRole());
		invitation.setStatus(AdminInvitation.InvitationStatus.PENDING);
		invitation.setInvitedBy(superAdmin);
		invitation.setExpiresAt(LocalDateTime.now().plusHours(24));

		log.info("DEV ONLY - Token: " + secureToken);

		adminInvitationRepo.save(invitation);

		String inviteLink = onboardingBaseUrl + "?token=" + secureToken + "&role=admin";
		
		emailService.sendAdminInviteEmail(
			    inviteAdminRequest.getEmail(), 
			    inviteAdminRequest.getFullName(), 
			    inviteLink, 
			    inviteAdminRequest.getRole()
			);
	}

	public String completeOnboarding(CompleteOnboardingRequestDTO request, String token) {

		AdminInvitation invitation = adminInvitationRepo.findByToken(token);

		if (invitation == null) {
			throw new ResourceAccessException("Invalid Token.");
		}
		if (invitation.getStatus() != AdminInvitation.InvitationStatus.PENDING) {
			throw new IllegalArgumentException("This token has already been used or expired.");
		}
		if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
			invitation.setStatus(AdminInvitation.InvitationStatus.EXPIRED);
			adminInvitationRepo.save(invitation);
			throw new IllegalArgumentException("The onboarding link has expired.");
		}

		Role assignedRole = invitation.getIntendedRole();
		
		String prefix = (assignedRole == Role.ADMIN) ? "ACS-ADM-" : "ACS-PRI-";
		String generatedUsername = idGenerator.generateIdWithoutYear(prefix);
		
		User newUser = new User();
		newUser.setUsername(generatedUsername);
		newUser.setEmail(invitation.getEmail());
		newUser.setFullName(invitation.getFullName());
		newUser.setPassword(passwordEncoder.encode(request.getPassword()));
		newUser.setRole(assignedRole);
		// newUser.setExperience(request.getExperience());
		// newUser.setAddress(request.getAddress());
		newUser.setApprovalStatus(true);
		newUser.setIsAvailable(true);

		User user = userRepository.save(newUser);

		Admin admin = new Admin();
		admin.setId(user.getUsername());
		admin.setEmail(user.getEmail());
		admin.setFullName(user.getFullName());
		admin.setPhone(request.getPhoneNo());
		admin.setAddress(request.getAddress());
		admin.setExperience(request.getExperience());
		admin.setUser(user);

		adminRepo.save(admin);

		invitation.setStatus(AdminInvitation.InvitationStatus.USED);
		adminInvitationRepo.save(invitation);

		return "Onboarding complete! Your assigned username is: " + generatedUsername;
	}

}
