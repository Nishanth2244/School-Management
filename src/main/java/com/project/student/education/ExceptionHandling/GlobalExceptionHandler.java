package com.project.student.education.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.project.student.education.DTO.ErrorResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ErrorManager;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(AlreadyExistsException ex) {

        com.project.student.education.DTO.ErrorResponse errorResponse = new ErrorResponse(
        		HttpStatus.CONFLICT, 
        		ex.getMessage(), 
        		HttpStatus.CONFLICT.value(),
        		LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);   
        
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("status", "ERROR");
		error.put("message", ex.getMessage());
		error.put("timestamp", LocalDateTime.now());

		// Explicitly return JSON to fix HttpMediaTypeNotAcceptableException
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.contentType(MediaType.APPLICATION_JSON)
				.body(error);
	}
	

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

    	ErrorResponse errorResponse = new ErrorResponse(
        		HttpStatus.BAD_REQUEST, 
        		ex.getMessage(), 
        		HttpStatus.BAD_REQUEST.value(),
        		LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    

    
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
    	
    	ErrorResponse errorResponse = new ErrorResponse(
    			HttpStatus.NOT_FOUND, 
    			ex.getMessage(), 
    			HttpStatus.NOT_FOUND.value(),
    			LocalDateTime.now());
    	
    	return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    	
    }
    
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex){
    	
    	ErrorResponse errorResponse = new ErrorResponse(
    			HttpStatus.UNAUTHORIZED, 
    			ex.getMessage(), 
    			HttpStatus.UNAUTHORIZED.value(),
    			LocalDateTime.now());
    	
    	return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    	
    }
    
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex){
    	
    	ErrorResponse errorResponse = new ErrorResponse(
    			HttpStatus.FORBIDDEN, 
    			ex.getMessage(), 
    			HttpStatus.FORBIDDEN.value(),
    			LocalDateTime.now());
    	
    	return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    	
    }
    
    
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex){
    	
    	ErrorResponse errorResponse = new ErrorResponse(
    			HttpStatus.CONFLICT, 
    			ex.getMessage(), 
    			HttpStatus.CONFLICT.value(),
    			LocalDateTime.now());
    	
    	return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    	
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex){
    	
    	ErrorResponse errorResponse = new ErrorResponse(
    			HttpStatus.BAD_REQUEST, 
    			ex.getMessage(), 
    			HttpStatus.BAD_REQUEST.value(),
    			LocalDateTime.now());
    	
    	return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    	
    }
}