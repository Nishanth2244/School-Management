package com.project.student.education.DTO;

public record ProfileResponse(
        Long userId,
        String username,
        String fullName,
        String email,
        String role,
        Boolean isActive,
        Object roleDetails
) {}