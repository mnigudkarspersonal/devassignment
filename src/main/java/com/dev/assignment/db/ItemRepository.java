package com.dev.assignment.db;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ItemRepository extends CrudRepository<Item, Long> {
	
	@Query("SELECT p FROM Item p WHERE (p.invoice_id) = (:invoiceId)")
	public List<Item> findByInvoiceId(@Param("invoiceId") String invoiceId);
	
	@Query("SELECT p FROM Item p WHERE (p.invoice_id) = (:invoiceId) AND (p.item_id) = (:itemId)")
	public Item findByInvoiceIdAndItemId(@Param("invoiceId") String invoiceId, @Param("itemId") String itemId);
	
	@Query("SELECT SUM(p.amount) as sum FROM Item p WHERE (p.invoice_id) = (:invoiceId)")
	public int findSumByInvoiceId(@Param("invoiceId") String invoiceId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Item p WHERE (p.invoice_id) = (:invoiceId)")
	public void deleteByInvoiceId(@Param("invoiceId") String invoiceId);
}
	