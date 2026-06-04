package com.project.student.education.service;
import com.project.student.education.DTO.UserListDTO;
import com.project.student.education.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.project.student.education.DTO.AdminDashboardDTO;
import com.project.student.education.repository.DriverRepo;
import com.project.student.education.repository.StudentRepository;
import com.project.student.education.repository.TeacherRepository;
import com.project.student.education.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DriverRepo driverRepo;
    private final UserRepository userRepository;

    public AdminDashboardDTO getDashboard() {

        AdminDashboardDTO dto = new AdminDashboardDTO();

        dto.setTotalStudents(studentRepository.countStudents());
        dto.setTotalTeachers(teacherRepository.countTeachers());
        dto.setTotalDrivers(driverRepo.count());
        dto.setTotalUsers(userRepository.count());

        return dto;
    }
    @Transactional
    public String updateStatus(
            String username,
            Boolean active) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        user.setIsAvailable(active);

        userRepository.save(user);

        return active
                ? "User enabled successfully"
                : "User disabled successfully";
    }
    public List<UserListDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private UserListDTO convertToDto(User user) {

        UserListDTO dto = new UserListDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setIsAvailable(user.getIsAvailable());

        return dto;
    }

}