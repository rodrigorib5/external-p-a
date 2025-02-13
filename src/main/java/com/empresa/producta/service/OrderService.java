package com.empresa.producta.service;

import com.empresa.producta.domain.DomainStatusMessage;
import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.dto.ProductDTO;
import com.empresa.producta.entity.OrderEntity;
import com.empresa.producta.entity.ProductEntity;
import com.empresa.producta.repository.OrderRepository;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  @Retry(name = "retryService")
  @Transactional(rollbackFor = Exception.class)
  public OrderEntity createOrder(OrderDTO orderDTO) {
    List<ProductDTO> products = orderDTO.getProducts();
    double totalAmount = products.stream().mapToDouble(ProductDTO::getPrice).sum();

    List<ProductEntity> productList = products.stream().map(ProductEntity::new).toList();

    OrderEntity order = new OrderEntity();
    order.setProducts(productList);
    order.setTotalAmount(totalAmount);
    order.setIdempotencyKey(orderDTO.getIdempotencyKey());
    order.setStatus(DomainStatusMessage.COLLECTED.name());

    return orderRepository.save(order);
  }

  public boolean existsByIdempotencyKey(String idempotencyKey) {
    return this.orderRepository.existsByIdempotencyKey(idempotencyKey);
  }
}
