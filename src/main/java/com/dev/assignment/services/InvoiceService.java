package com.dev.assignment.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dev.assignment.db.Invoice;
import com.dev.assignment.db.InvoiceDAO;
import com.dev.assignment.db.Item;
import com.dev.assignment.db.ItemDAO;
import com.dev.assignment.exception.InvoiceException;
import com.dev.assignment.model.InvoiceModel;
import com.dev.assignment.model.ItemModel;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is the service implementation of CRUD operations for invoice and it's items.
 * 
 * @author mnigudkar
 *
 */
@SuppressWarnings("deprecation")
@Service
@Slf4j
public class InvoiceService {
	
	@Autowired
	private InvoiceDAO invoiceRepo;
	
	@Autowired
	private ItemDAO itemRepo;
	
	/**
	 * Create invoice and it's items.
	 * 
	 * @param invoiceInfo
	 * @return
	 */
	public InvoiceModel createInvoice(InvoiceModel invoiceInfo) {
		log.info("createInvoice called....");
		validateInvoiceModel(true, invoiceInfo);
		
		Invoice entity = mapInvoiceModelToEntity(invoiceInfo);
		Invoice entityUpdated = invoiceRepo.save(entity);
		invoiceInfo.setInvoiceId(entityUpdated.getInvoice_id());
		
		List<Item> itemList = mapItemModelToEntity(invoiceInfo);
		List<Item> itemListUpdated = itemRepo.save(itemList);
		ItemModel[] itemModel = mapItemEntityToModel(itemListUpdated);
		invoiceInfo.setItemList(itemModel);
		invoiceInfo.setTotalAmount(new Integer (itemRepo.findSumByInvoiceId(entityUpdated.getInvoice_id())).toString());
		return invoiceInfo;
	}
	
	/**
	 * Update invoice and it's items.
	 * @param invoiceInfo
	 * @return
	 */
	public InvoiceModel updateInvoice(InvoiceModel invoiceInfo) {
		log.info("updateInvoice called....");
		if (!invoiceInfo.isDeleted()) {
			validateInvoiceModel(false, invoiceInfo);
		}
		Invoice invoiceEntity = invoiceRepo.findByInvoiceId(invoiceInfo.getInvoiceId());
		if(invoiceEntity == null) {
			log.error("Invoice not found with invoiceId {}", invoiceInfo.getInvoiceId());
			throw new EntityNotFoundException("Invoice Id " + invoiceInfo.getInvoiceId() + " not found.");
		}
		mapInvoiceModelToEntity(invoiceInfo, invoiceEntity);
		Invoice entityUpdated = invoiceRepo.save(invoiceEntity);
		
		List<Item> itemList = null;
		if(invoiceInfo.getItemList() != null && invoiceInfo.getItemList().length > 0) {
			verifyItemId(invoiceInfo.getItemList(), invoiceInfo.getInvoiceId());
			itemList = mapItemModelToEntityIfModified(invoiceInfo);
			itemRepo.save(itemList);
		}
		mapInvoiceEntityToInvoiceModel(entityUpdated, itemList, invoiceInfo);
		return invoiceInfo;
	}

	/**
	 * Delete invoice and it's items.
	 * @param invoiceInfo
	 * @return
	 */
	public InvoiceModel deleteInvoice(InvoiceModel invoiceInfo) {
		log.info("deleteInvoice called....");
		if(invoiceInfo.getInvoiceId() == null || "".equalsIgnoreCase(invoiceInfo.getInvoiceId())) {
			log.error("Invoice id is not present in the rquest.");;
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Invoice id is required");
		}
		Invoice invoiceEntity = invoiceRepo.findByInvoiceId(invoiceInfo.getInvoiceId());
		if(invoiceEntity == null) {
			log.error("Invoice not found with invoiceId {}", invoiceInfo.getInvoiceId());
			throw new EntityNotFoundException("Invoice Id " + invoiceInfo.getInvoiceId() + " not found.");
		}
		itemRepo.deleteByInvoiceId(invoiceInfo.getInvoiceId());
		invoiceRepo.deleteByInvoiceId(invoiceInfo.getInvoiceId());
		invoiceInfo.setDeleted(true);
		return invoiceInfo;
	}
		
	/**
	 * Get invoice and it's items.
	 * @param invoiceId
	 * @return
	 */
	public InvoiceModel getInvoice(String invoiceId) {
		Invoice invoiceEntity = invoiceRepo.findByInvoiceId(invoiceId);
		if(invoiceEntity == null) {
			log.error("Invoice not found with invoiceId {}", invoiceId);
			throw new EntityNotFoundException("Invoice Id " + invoiceId + " not found.");
		}
		List<Item> itemEntities = itemRepo.findByInvoiceId(invoiceId);
		if(itemEntities == null || itemEntities.size() == 0) {
			log.error("No Item found for invoiceId {}", invoiceId);
			throw new EntityNotFoundException("No Item found for invoiceId " + invoiceId);
		}
		int sum = itemRepo.findSumByInvoiceId(invoiceId);
		InvoiceModel model = new InvoiceModel();
		model.setDueDate(invoiceEntity.getDueDate());
		model.setEmail(invoiceEntity.getEmail());
		model.setInvoiceId(invoiceId);
		model.setItemList(mapItemEntityToModel(itemEntities));
		model.setName(invoiceEntity.getName());
		model.setTotalAmount(new Integer(sum).toString());
		return model;
	}
	
	/**
	 * This method maps request model to DB entity model for invoice.
	 * @param invoiceInfo
	 * @return
	 */
	private Invoice mapInvoiceModelToEntity(InvoiceModel invoiceInfo) {
		Invoice entity = new Invoice();
		entity.setDueDate(invoiceInfo.getDueDate());
		entity.setEmail(invoiceInfo.getEmail());
		entity.setInvoice_id(UUID.randomUUID().toString());
		entity.setName(invoiceInfo.getName());
		return entity;
	}
	
	/**
	 * Map non null attributes of invoice request model to DB entity model.
	 * @param invoiceInfo
	 * @param invoiceEntity
	 */
	private void mapInvoiceModelToEntity(InvoiceModel invoiceInfo, Invoice invoiceEntity) {
		if(invoiceInfo.getDueDate() != null) {
			invoiceEntity.setDueDate(invoiceInfo.getDueDate());
		}
		if(invoiceInfo.getEmail() != null) {
			invoiceEntity.setEmail(invoiceInfo.getEmail());
		}
		if(invoiceInfo.getName() != null) {
			invoiceEntity.setName(invoiceInfo.getName());
		}
	}
	
	/**
	 * Maps DB entity model to response model for Invoice and entity.
	 * @param entity
	 * @param Items
	 * @param invoiceInfo
	 */
	private void mapInvoiceEntityToInvoiceModel(Invoice entity, List<Item> Items, InvoiceModel invoiceInfo) {
		invoiceInfo.setDueDate(entity.getDueDate());
		invoiceInfo.setEmail(entity.getEmail());
		if (Items != null && Items.size() > 0) {
			invoiceInfo.setItemList(mapItemEntityToModel(Items));
		}
		invoiceInfo.setName(entity.getName());
	}
	
	/**
	 * This method maps a list of request model to the list of DB entity model for Item.
	 * @param invoiceInfo
	 * @return
	 */
	private List<Item> mapItemModelToEntity(InvoiceModel invoiceInfo) {
		ArrayList<Item> list = new ArrayList<Item>();
		if (invoiceInfo.getItemList() != null && invoiceInfo.getItemList().length > 0) {
			ArrayList<ItemModel> modelList = new ArrayList<ItemModel>(Arrays.asList(invoiceInfo.getItemList()));
			for (int i = 0; i < modelList.size(); i++) {
				Item item = new Item();
				item.setInvoice_id(invoiceInfo.getInvoiceId());
				item.setDescription(modelList.get(i).getDescription());
				item.setAmount(new Integer(modelList.get(i).getAmount()).intValue());
				item.setItem_id(UUID.randomUUID().toString());
				list.add(item);
			}
		}
		return list;
	}
	
	private List<Item> mapItemModelToEntityIfModified(InvoiceModel invoiceInfo) {
		ArrayList<Item> list = new ArrayList<Item>();
		if (invoiceInfo.getItemList() != null && invoiceInfo.getItemList().length > 0) {
			ArrayList<ItemModel> modelList = new ArrayList<ItemModel>(Arrays.asList(invoiceInfo.getItemList()));
			for (int i = 0; i < modelList.size(); i++) {
				Item item = itemRepo.findByInvoiceIdAndItemId(invoiceInfo.getInvoiceId(), modelList.get(i).getItemId());
				if (modelList.get(i).getDescription() != null) {
					item.setDescription(modelList.get(i).getDescription());
				}
				if (modelList.get(i).getAmount() != null) {
					item.setAmount(new Integer(modelList.get(i).getAmount()).intValue());
				}
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * This method maps list of DB entity model for Item to the list of response model for Item 
	 * @param list
	 * @return
	 */
	private ItemModel[] mapItemEntityToModel(List<Item> list) {
		if(list == null) return null;
		ItemModel[] itemModel = new ItemModel[list.size()];
		for(int i=0; i<list.size(); i++) {
			ItemModel item = new ItemModel(); 
			item.setAmount(new Integer(list.get(i).getAmount()).toString());
			item.setDescription(list.get(i).getDescription());
			item.setItemId(list.get(i).getItem_id());
			itemModel[i] = item;
		}
		return itemModel;
	}
	
	/**
	 * Verify if the item is specified in the request is associated with specified invoice id.
	 * 
	 * @param items
	 * @param invoiceId
	 */
	private void verifyItemId(ItemModel[] items, String invoiceId) {
		for(int i = 0; i<items.length; i++) {
			String itemId = items[i].getItemId();
			if(itemRepo.findByInvoiceIdAndItemId(invoiceId, items[i].getItemId()) == null) {
				log.error("No Item found with invoice id {} and item id {} ", invoiceId, itemId);
				throw new EntityNotFoundException("No Item found with invoice id " + invoiceId +  " and item id " + itemId);
			}
		}
	}
	
	/**
	 * Validate request.
	 * 
	 * @param isCreate
	 * @param invoiceInfo
	 */
	private void validateInvoiceModel(boolean isCreate, InvoiceModel invoiceInfo) {
		if(isCreate && (invoiceInfo.getEmail() == null || "".equalsIgnoreCase(invoiceInfo.getEmail()))) {
			log.error("Email is required.");
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Email is required.");
		}
		
		if(invoiceInfo.getEmail() != null && !isValidEmailAddress(invoiceInfo.getEmail())) {
			log.error("Invalid email address.");
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Invalid email address.");
		}
		
		if(!isCreate && invoiceInfo.getEmail() != null && "".equalsIgnoreCase(invoiceInfo.getEmail())) {
			log.error("Email can't be set to empty.");
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Email can't be empty.");
		}
		
		if(isCreate && invoiceInfo.getDueDate() == null) {
			log.error("Due date is required.");
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Due date is required.");
		}
		
		if((isCreate && (invoiceInfo.getItemList() == null || invoiceInfo.getItemList().length == 0))) {
			log.error("Atleast one item is required for the invoice.");;
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Atleast one item is required for the invoice.");
		}
		
		if(invoiceInfo.getItemList() != null && invoiceInfo.getItemList().length > 0) {
			for(int i = 0; i < invoiceInfo.getItemList().length; i++) {
				validateItemModel(isCreate, invoiceInfo.getItemList()[i]);
			}
		}
	}
	
	private void validateItemModel(boolean isCreate, ItemModel item) {
		if(isCreate && (item.getDescription() == null || "".equalsIgnoreCase(item.getDescription()))) {
			log.error("Item description is required.");
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Item description is required.");
		}
		
		if(!isCreate && item.getItemId() == null) {
			log.error("Item id is required for update.");
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Item id is required for update.");
		}
		
		if(!isCreate && item.getDescription() != null && "".equalsIgnoreCase(item.getDescription())) {
			log.error("Item description can't be empty.");
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Item description is required.");
		}
		
		if(isCreate && item.getAmount() == null) {
			log.error("Item amount is required.");
			throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Item amount is required.");
		}
		if(item.getAmount() != null) {
			try {
				new Integer(item.getAmount());
			} catch (NumberFormatException ex) {
				log.error("Item amount is required to be integer.");
				throw new InvoiceException(HttpStatus.BAD_REQUEST.value(), "Item amount is required to be integer.");
			}
		}
	}
	
	private boolean isValidEmailAddress(String email) {
		@SuppressWarnings("deprecation")
		boolean isValid = EmailValidator.getInstance().isValid(email);
		return isValid;
	}
}