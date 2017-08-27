package com.dev.assignment.db;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public class ItemDAO {
	
	@Autowired
	private ItemRepository repository;
	
	public List<Item> findByInvoiceId(@Param("InvoiceId") String invoiceId) {
		return repository.findByInvoiceId(invoiceId);
	}
	
	public Item findByInvoiceIdAndItemId(@Param("InvoiceId") String invoiceId, @Param("itemId") String itemId) {
		return repository.findByInvoiceIdAndItemId(invoiceId, itemId);
	}
	
	public int findSumByInvoiceId(@Param("InvoiceId") String invoiceId) {
		return repository.findSumByInvoiceId(invoiceId);
	}
	public List<Item> save(List<Item> entities) {
		return (List<Item>) repository.save(entities);
	}
	
	public void deleteByInvoiceId(@Param("InvoiceId") String invoiceId) {
		repository.deleteByInvoiceId(invoiceId);
	}
}