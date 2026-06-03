package com.project.student.education.service;

import com.project.student.education.DTO.ProfileResponse;
import com.project.student.education.config.SecurityUtil;
import com.project.student.education.entity.User;
import com.project.student.education.enums.Role;
import com.project.student.education.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final SecurityUtil securityUtil;
    private final SuperAdminRepo superAdminRepo;
    private final AdminRepo adminRepo;
    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;
    private final DriverRepo driverRepo;

    public ProfileResponse getMyProfile() {
    	
        User user = securityUtil.getCurrentUser();
        Role role = user.getRole();
        
        Object roleSpecificData = null;

        switch (role) {
            case SUPER_ADMIN -> {
                roleSpecificData = superAdminRepo.findByUser(user).orElse(null);
            }
            case ADMIN, PRINCIPAL -> {
                roleSpecificData = adminRepo.findByUser(user).orElse(null);
            }
            case STUDENT -> {
                roleSpecificData = studentRepo.findByUser(user).orElse(null);
            }
            case TEACHER -> {
                roleSpecificData = teacherRepo.findByUser(user).orElse(null);
            }
            case DRIVER -> {
                roleSpecificData = driverRepo.findByUser(user).orElse(null);
            }
            default -> roleSpecificData = "No additional role details found.";
        }

        // 3. User table data + Role table data merge chesi return chesthunnam
        return new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                role.name(),
                user.getIsAvailable(),
                roleSpecificData
        );
    }
}