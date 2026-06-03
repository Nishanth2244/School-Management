package com.project.student.education.service;

import com.project.student.education.DTO.ProfileResponse;
import com.project.student.education.DTO.StudentDTO;
import com.project.student.education.DTO.TeacherDTO; 
import com.project.student.education.config.SecurityUtil;
import com.project.student.education.entity.User;
import com.project.student.education.enums.Role;
import com.project.student.education.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper; 
    
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
            case STUDENT -> {
                roleSpecificData = studentRepo.findByUser(user)
                        .map(student -> modelMapper.map(student, StudentDTO.class))
                        .orElse(null);
            }
            case TEACHER -> {
                roleSpecificData = teacherRepo.findByUser(user)
                        .map(teacher -> modelMapper.map(teacher, TeacherDTO.class))
                        .orElse(null);
            }
            case SUPER_ADMIN -> {
                roleSpecificData = superAdminRepo.findByUser(user).orElse(null);
            }
            case ADMIN, PRINCIPAL -> {
                roleSpecificData = adminRepo.findByUser(user).orElse(null);
            }
            case DRIVER -> {
                roleSpecificData = driverRepo.findByUser(user).orElse(null);
            }
            default -> roleSpecificData = "No additional role details found.";
        }

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