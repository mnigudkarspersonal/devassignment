package com.dev.assignment.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class InvoiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	private String message;

	public InvoiceException(HttpStatus status) {
		this.status = status;
	}

	public InvoiceException(HttpStatus status, Throwable ex) {
		this.status = status;
		this.message = "Unexpected error";
	}

	public InvoiceException(HttpStatus status, String message, Throwable ex) {
		this.status = status;
		this.message = message;
	}
	
	public InvoiceException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}