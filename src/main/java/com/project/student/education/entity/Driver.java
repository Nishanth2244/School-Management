package com.project.student.education.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Driver {
	
	@Id
	private String id;
	
	private String fullName;
	private String experience;
	private String address;
	private String licenseNo;
	private String phoneNo;

}
