package com.dev.assignment.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public class InvoiceDAO {
	
	@Autowired
	private InvoiceRepository repository;
	
	public Invoice findByInvoiceId(@Param("InvoiceId") String invoiceId) {
		return repository.findByInvoiceId(invoiceId);
	}
	
	public void deleteByInvoiceId(@Param("InvoiceId") String invoiceId) {
		repository.deleteByInvoiceId(invoiceId);
	}
	
	public Invoice save(Invoice entity) {
		return repository.save(entity);
	}
}