package com.casestudy.catalogue.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "data", "currentPage", "totalPage", "currentSize", "totalSize" })
public class CatalogueItemResponsePaged {
	private List<CatalogueItemResponse> data;
	private Integer currentPage;
	private Integer totalPage;
	private Integer currentSize;
	private Long totalSize;
	
	public static CatalogueItemResponsePaged createFromPage(Page<CatalogueItemResponse> page) {
		CatalogueItemResponsePaged responsePaged = new CatalogueItemResponsePaged();
		responsePaged.setData(page.getContent());
		responsePaged.setCurrentPage(page.getPageable().getPageNumber() + 1);
		responsePaged.setTotalPage(page.getTotalPages());
		responsePaged.setCurrentSize(page.getNumberOfElements());
		responsePaged.setTotalSize(page.getTotalElements());
		return responsePaged;
	}

	public List<CatalogueItemResponse> getData() {
		return data;
	}

	public void setData(List<CatalogueItemResponse> data) {
		this.data = data;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(Integer currentSize) {
		this.currentSize = currentSize;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

}
