package com.project.student.education.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentProfileUpdateDTO {

    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String nationality;
    private String religion;
    private String category;
    private String aadhaarNumber;

    private String address;
    private String city;
    private String state;
    private String pincode;
    private String contactNumber;

    private String motherName;
    private String motherContact;

    private String guardianName;
    private String guardianContact;

    private String emergencyContactName;
    private String emergencyContactNumber;
}