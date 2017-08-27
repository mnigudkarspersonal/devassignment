package com.dev.assignment.db;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
	
	@Query("SELECT p FROM Invoice p WHERE (p.invoice_id) = (:invoiceId)")
	public Invoice findByInvoiceId(@Param("invoiceId") String invoiceId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Invoice p WHERE (p.invoice_id) = (:invoiceId)")
	public void deleteByInvoiceId(@Param("invoiceId") String invoiceId);
}
	