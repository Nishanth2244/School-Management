package com.project.student.education.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.student.education.DTO.AdminCreateRequestDTO;
import com.project.student.education.entity.Admin;
import com.project.student.education.entity.User;
import com.project.student.education.enums.Role;
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

	public void addAdmin(AdminCreateRequestDTO adminCreateRequestDTO) {
		
		if (userRepository.findByUsername(adminCreateRequestDTO.getUserName()).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }
        if (userRepository.findByEmail(adminCreateRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }

        User authUser = User.builder()
                .username(adminCreateRequestDTO.getUserName())
                .password(passwordEncoder.encode(adminCreateRequestDTO.getPassword()))
                .email(adminCreateRequestDTO.getEmail())
                .role(Role.ADMIN)
                .approvalStatus(true)
                .isAvailable(true)
                .build();
        
        User savedUser = userRepository.save(authUser);
        
        
        Admin adminProfile = Admin.builder()
                .fullName(adminCreateRequestDTO.getFullName())
                .phone(adminCreateRequestDTO.getPhone())
                .email(adminCreateRequestDTO.getEmail())
                .user(savedUser)
                .build();

        Admin savedAdmin = adminRepo.save(adminProfile);

        log.info("✅ Admin created successfully: {}", adminCreateRequestDTO.getUserName());
	}

}
