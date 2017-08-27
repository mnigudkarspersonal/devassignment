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
	private int statusCode;
	private String message;

	public InvoiceException(int statusCode) {
		this.statusCode = statusCode;
	}

	public InvoiceException(int statusCode, Throwable ex) {
		this.statusCode = statusCode;
		this.message = "Unexpected error";
	}

	public InvoiceException(int statusCode, String message, Throwable ex) {
		this.statusCode = statusCode;
		this.message = message;
	}
	
	public InvoiceException(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

}