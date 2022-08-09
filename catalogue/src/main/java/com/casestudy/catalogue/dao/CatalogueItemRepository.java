package com.casestudy.catalogue.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casestudy.catalogue.entity.CatalogueItem;

@Repository
public interface CatalogueItemRepository extends JpaRepository<CatalogueItem, Long> {
	Optional<CatalogueItem> findByProductId(String productId);
	
	List<CatalogueItem> findAllByOrderByTitle();

	Page<CatalogueItem> findAllByOrderByTitle(Pageable pageable);

	List<CatalogueItem> findByTitleOrderByPrice(String title);

	Page<CatalogueItem> findByTitleOrderByPrice(String title, Pageable pageable);

	List<CatalogueItem> findByCategoryOrderByTitleAscPriceAsc(String category);

	Page<CatalogueItem> findByCategoryOrderByTitleAscPriceAsc(String category, Pageable pageable);
}
