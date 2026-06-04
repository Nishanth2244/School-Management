package com.project.student.education.DTO;

import com.project.student.education.enums.Role;

import lombok.Data;

@Data
public class UserListDTO {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Role role;
    private Boolean isAvailable;

}