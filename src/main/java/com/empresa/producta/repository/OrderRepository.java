package com.empresa.producta.repository;

import com.empresa.producta.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  boolean existsByIdempotencyKey(String idempotencyKey);
}
