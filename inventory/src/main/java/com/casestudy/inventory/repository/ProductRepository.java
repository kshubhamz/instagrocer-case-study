package com.casestudy.inventory.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.casestudy.inventory.entity.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	@Query(value = "{title: { $regex: /?0/, $options: 'i' }, category: { $regex: /?1/, $options: 'i' }, description: { $regex: /?2/, $options: 'i' }}", sort = "{title: 1}")
	List<Product> findAllProductsSortByTitle(String title, String category, String description);

	@Query(value = "{title: { $regex: /?0/, $options: 'i' }, category: { $regex: /?1/, $options: 'i' }, description: { $regex: /?2/, $options: 'i' }}", sort = "{title: 1}")
	Page<Product> findAllProductsSortByTitle(String title, String category, String description, Pageable pageable);

	@Query(value = "{title: { $regex: /?0/, $options: 'i' }, category: { $regex: /?1/, $options: 'i' }, description: { $regex: /?2/, $options: 'i' }}", fields = "{ownerUsername: 0}", sort = "{title: 1}")
	List<Product> findAllProductsSortByTitleWithoutOwner(String title, String category, String description);

	@Query(value = "{title: { $regex: /?0/, $options: 'i' }, category: { $regex: /?1/, $options: 'i' }, description: { $regex: /?2/, $options: 'i' }}", fields = "{ownerUsername: 0}", sort = "{title: 1}")
	Page<Product> findAllProductsSortByTitleWithoutOwner(String title, String category, String description,
			Pageable pageable);

	List<Product> findByOwnerUsernameOrderByTitle(String ownerUsername);

	Page<Product> findByOwnerUsernameOrderByTitle(String ownerUsername, Pageable pageable);
}
