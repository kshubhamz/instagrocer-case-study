package com.casestudy.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casestudy.order.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
