package com.dev.assignment.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class InvoiceModel {
	String invoiceId;
	String name;
	String email;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd" , timezone="PST")
	Date dueDate; 
	ItemModel[] itemList;
	String totalAmount;
	boolean deleted;
}