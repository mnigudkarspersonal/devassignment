package com.dev.assignment.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;

import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class InvoiceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(EntityNotFoundException.class)
	   protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
	       InvoiceException error = new InvoiceException(HttpStatus.NOT_FOUND);
	       error.setMessage(ex.getMessage());
	       return new ResponseEntity<>(error, error.getStatus());
	   }
}