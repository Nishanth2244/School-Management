package com.project.student.education.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @Column(name = "student_id", length = 50)
    private String studentId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String nationality;
    private String religion;
    private String category;
    private String aadhaarNumber;
    private String grade;
    private String section;

    private String academicYear;
    private LocalDate joiningDate;
    private String rollNumber;
    private String classTeacherId;

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

    private String profileImageUrl;
    private Boolean active;

    @OneToOne
    @JoinColumn(name = "admission_number", referencedColumnName = "admissionNumber")
    private Admission admission;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // This object association owns database write/update states for the column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_section_id", referencedColumnName = "classSectionId")
    private ClassSection classSection;

    private Double totalFee;


    @Column(name = "class_section_id", insertable = false, updatable = false)
    private String classSectionId;
}