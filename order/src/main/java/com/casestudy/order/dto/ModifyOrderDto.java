package com.casestudy.order.dto;

import javax.validation.constraints.NotNull;

public class ModifyOrderDto {
	@NotNull(message = "Cancel must be defined.")
	private Boolean cancel;

	public Boolean getCancel() {
		return cancel;
	}

	public void setCancel(Boolean cancel) {
		this.cancel = cancel;
	}

}
