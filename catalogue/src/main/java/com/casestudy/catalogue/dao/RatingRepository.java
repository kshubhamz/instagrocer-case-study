package com.casestudy.catalogue.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casestudy.catalogue.entity.CatalogueItem;
import com.casestudy.catalogue.entity.ItemRating;

@Repository
public interface RatingRepository extends JpaRepository<ItemRating, Long> {
	Optional<ItemRating> findByUsernameAndCatalogueItem(String username, CatalogueItem catalogueItem);
}
