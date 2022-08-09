package com.casestudy.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.casestudy.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
