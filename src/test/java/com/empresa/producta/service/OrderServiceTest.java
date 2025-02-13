package com.empresa.producta.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.entity.OrderEntity;
import com.empresa.producta.repository.OrderRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @InjectMocks
  private OrderService orderService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createOrderWithEmptyProductList() {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setProducts(List.of());
    orderDTO.setIdempotencyKey("unique-key");

    OrderEntity orderEntity = new OrderEntity();
    orderEntity.setProducts(List.of());
    orderEntity.setTotalAmount(0.0);
    orderEntity.setIdempotencyKey("unique-key");
    orderEntity.setStatus("COLLECTED");

    when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

    OrderEntity result = orderService.createOrder(orderDTO);

    assertEquals(0.0, result.getTotalAmount());
    assertEquals("COLLECTED", result.getStatus());
  }

  @Test
  void checkIfOrderExistsByIdempotencyKey() {
    String idempotencyKey = "unique-key";

    when(orderRepository.existsByIdempotencyKey(idempotencyKey)).thenReturn(true);

    boolean exists = orderService.existsByIdempotencyKey(idempotencyKey);

    assertEquals(true, exists);
  }

  @Test
  void checkIfOrderDoesNotExistByIdempotencyKey() {
    String idempotencyKey = "non-existent-key";

    when(orderRepository.existsByIdempotencyKey(idempotencyKey)).thenReturn(false);

    boolean exists = orderService.existsByIdempotencyKey(idempotencyKey);

    assertEquals(false, exists);
  }
}