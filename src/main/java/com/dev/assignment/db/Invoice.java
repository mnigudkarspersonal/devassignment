package com.dev.assignment.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String invoice_id;
	
	private String name;
	
	private String email;
	
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date dueDate;
	
}