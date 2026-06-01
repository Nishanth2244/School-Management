package com.project.student.education.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeacherRegistrationDTO {

    private String token;

    private String teacherName;

    private String email;

    private String phone;

    private String qualification;

    private String gender;

    private int experience;

    private String address;

    private List<String> subjectIds;
}