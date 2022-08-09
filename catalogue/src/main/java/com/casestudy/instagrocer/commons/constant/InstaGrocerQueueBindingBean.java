package com.casestudy.instagrocer.commons.constant;

public final class InstaGrocerQueueBindingBean {
	
	public static final String INVENTORY_ITEM_CREATED = "InventoryItemCreatedQueueBinding";
	public static final String INVENTORY_ITEM_UPDATED = "InventoryItemUpdatedQueueBinding";
	public static final String INVENTORY_ITEM_DELETED = "InventoryItemDeletedQueueBinding";
	public static final String ORDER_CREATED = "OrderCreatedQueueBinding";
	public static final String ORDER_FAILURE = "OrderFailureQueueBinding";
	public static final String ORDER_SUCCESS = "OrderSuccessQueueBinding";
	
	private InstaGrocerQueueBindingBean() {
		
	}
}
