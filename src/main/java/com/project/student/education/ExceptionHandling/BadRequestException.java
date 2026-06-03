package com.project.student.education.ExceptionHandling;

public class BadRequestException extends RuntimeException{
	
	public BadRequestException(String message) {
		
		super(message);
	}

}
