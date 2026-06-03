package com.project.student.education.ExceptionHandling;

public class UnauthorizedException extends RuntimeException {
	
	public UnauthorizedException(String message) {
		super(message);
	}

}
