package com.project.student.education.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.project.student.education.entity.User;
import com.project.student.education.entity.superAdmin;
import com.project.student.education.enums.Role;
import com.project.student.education.repository.SuperAdminRepo;
import com.project.student.education.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SuperAdminRepo superAdminRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional 
    public void run(String... args) throws Exception {
        
        String superAdminUsername = "superadmin";

        if (userRepository.findByUsername(superAdminUsername).isEmpty()) {
            
            User authUser = User.builder()
                    .username(superAdminUsername)
                    .password(passwordEncoder.encode("superadmin@123"))
                    .email("superadmin@school.com")
                    .role(Role.SUPER_ADMIN) 
                    .approvalStatus(true)
                    .isAvailable(true)
                    .build();

            User savedUser = userRepository.save(authUser);

            superAdmin superAdminProfile = superAdmin.builder()
                    .fullName("Super Admin")
                    .phone("9999999999")
                    .user(savedUser)
                    .build();

            superAdminRepo.save(superAdminProfile);
            
            log.info("🔥 Superadmin Auth & Profile tables initialized successfully!");
        } else {
            log.info("✅ Superadmin already exists. Skipping initialization.");
        }
    }
}