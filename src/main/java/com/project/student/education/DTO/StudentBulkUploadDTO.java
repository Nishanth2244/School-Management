package com.project.student.education.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentBulkUploadDTO {

    private String fullName;

    private String email;

    private String grade;

    private String section;

    private String academicYear;

    private String fatherName;

    private String fatherContact;

    private Double totalFee;
}