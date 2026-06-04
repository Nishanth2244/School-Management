package com.project.student.education.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverStudentDTO {

    private String studentId;
    private String studentName;
    private String pickupStop;
    private String routeName;
    private String status;
}