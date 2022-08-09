package com.casestudy.instagrocer.commons.constant;

public final class InstaGrocerEvent {

	public static final String ORDER_CREATED = "order-created";
	public static final String ORDER_FAILURE = "order-failure";
	public static final String ORDER_SUCCESS = "order-success";
	public static final String PRODUCT_ORDER_UNAVAILABLE = "order-created-product-unavailable";
	public static final String PRODUCT_SOME_ORDER_UNAVAILABLE = "order-created-some-product-unavailable";
	public static final String INVENTORY_ITEM_CREATED = "inventory-item-created";
	public static final String INVENTORY_ITEM_UPDATED = "inventory-item-updated";
	public static final String INVENTORY_ITEM_DELETED = "inventory-item-deleted";
	
	private InstaGrocerEvent() {

	}

}
