package com.project.student.education.DTO;

import lombok.Data;

@Data

public class AttendanceDriver { 



    private String studentId;
    private String status; // PRESENT / ABSENT
    private String tripType; // PICKUP / DROP

}