package com.project.student.education.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentUpdateRequestDTO {
    
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String nationality;
    private String religion;
    private String category;
    private String aadhaarNumber;
    
    private LocalDate joiningDate;
    private String rollNumber;
    private Boolean active;
    
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String contactNumber;
    private String email;

    private String fatherName;
    private String fatherContact;
    private String motherName;
    private String motherContact;
    private String guardianName;
    private String guardianContact;
    private String emergencyContactName;
    private String emergencyContactNumber;
}