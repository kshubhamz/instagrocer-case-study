package com.casestudy.instagrocer.commons.constant;

public final class InstaGrocerRouting {

	public static final String INVENTORY_ITEM_CREATE_ROUTE = "service.inventory.item-created";
	public static final String INVENTORY_ITEM_DELETE_ROUTE = "service.inventory.item-deleated";
	public static final String INVENTORY_ITEM_UPDATE_ROUTE = "service.inventory.item-updated";
	public static final String ORDER_CREATED = "service.order.order-created";
	public static final String ORDER_FAILURE = "service.order.order-failure";
	public static final String ORDER_SUCCESS = "service.order.order-success";
	public static final String PRODUCT_ORDER_UNAVAILABLE_ROUTE = "service.order.product-order-unavailable";
	public static final String PRODUCT_ORDER_SOME_UNAVAILABLE_ROUTE = "service.order.some-product-order-unavailable";
	
	private InstaGrocerRouting() {
		
	}

}
