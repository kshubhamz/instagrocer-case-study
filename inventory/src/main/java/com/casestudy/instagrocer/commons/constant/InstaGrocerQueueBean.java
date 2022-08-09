package com.casestudy.instagrocer.commons.constant;

public final class InstaGrocerQueueBean {
	public static final String INVENTORY_ITEM_CREATED = "InventoryItemCreatedQueue";
	public static final String INVENTORY_ITEM_UPDATED = "InventoryItemUpdatedQueue";
	public static final String INVENTORY_ITEM_DELETED = "InventoryItemDeletedQueue";
	public static final String ORDER_CREATED = "OrderCreatedQueue";
	public static final String ORDER_FAILURE = "OrderFailureQueue";
	public static final String ORDER_SUCCESS = "OrderSuccessQueue";
	public static final String ORDER_UNAVAILABLE_PRODUCT = "OrderUnAvailableQueue";
	
	private InstaGrocerQueueBean() {
		
	}
}
