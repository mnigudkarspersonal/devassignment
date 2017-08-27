package com.dev.assignment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dev.assignment.model.InvoiceModel;
import com.dev.assignment.services.InvoiceService;

/**
 * This controller class is an entry point into spring boot application for rest API.
 *  It defines the request mapping.
 *  
 * @author mnigudkar
 *
 */
@ComponentScan({"com.dev.assignment.model", "com.dev.assignment.db", "com.dev.assignment.services"})
@RestController
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.dev.assignment.db"})
@EnableJpaRepositories(basePackages = {"com.dev.assignment.db"})
@RequestMapping("invoice")
@Validated
public class InvoiceController{
	
	@Autowired
	private InvoiceService invoiceService;
	
	@RequestMapping(value = "", method = RequestMethod.POST)    
	public ResponseEntity<?> createOrUpdateInvoice(@RequestBody InvoiceModel invoiceInfo) throws Exception {
		
		if(invoiceInfo.isDeleted()) {
			return new ResponseEntity<InvoiceModel>(invoiceService.deleteInvoice(invoiceInfo), HttpStatus.ACCEPTED);
		}
		
		if(invoiceInfo.getInvoiceId() == null || "".equalsIgnoreCase(invoiceInfo.getInvoiceId())) {
			return new ResponseEntity<InvoiceModel>(invoiceService.createInvoice(invoiceInfo), HttpStatus.CREATED);
		}
		return new ResponseEntity<InvoiceModel>(invoiceService.updateInvoice(invoiceInfo), HttpStatus.ACCEPTED);
    }
	
	@RequestMapping(value = "", method = RequestMethod.GET)    
	public ResponseEntity<?> getInvoice(@RequestBody Map<String, Object> request) throws Exception {
		return new ResponseEntity<InvoiceModel>(invoiceService.getInvoice(request.get("invoiceId").toString()), HttpStatus.FOUND);
    }
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(InvoiceController.class, args);
	}
}