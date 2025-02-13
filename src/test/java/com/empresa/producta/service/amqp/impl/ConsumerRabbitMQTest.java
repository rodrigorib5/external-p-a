package com.empresa.producta.service.amqp.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;

class ConsumerRabbitMQTest {

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private OrderService orderService;

  @InjectMocks
  private ConsumerRabbitMQ consumerRabbitMQ;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void receiveOrderSuccessfully() throws IOException {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setIdempotencyKey("unique-key");
    Message message = mock(Message.class);
    when(message.getBody()).thenReturn("{\"idempotencyKey\":\"unique-key\"}".getBytes());
    when(objectMapper.readValue(message.getBody(), OrderDTO.class)).thenReturn(orderDTO);
    when(orderService.existsByIdempotencyKey("unique-key")).thenReturn(false);

    consumerRabbitMQ.receiveOrder(message);

    verify(orderService).createOrder(orderDTO);
  }

  @Test
  void receiveOrderWithDuplicateIdempotencyKey() throws IOException {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setIdempotencyKey("unique-key");
    Message message = mock(Message.class);
    when(message.getBody()).thenReturn("{\"idempotencyKey\":\"unique-key\"}".getBytes());
    when(objectMapper.readValue(message.getBody(), OrderDTO.class)).thenReturn(orderDTO);
    when(orderService.existsByIdempotencyKey("unique-key")).thenReturn(true);

    consumerRabbitMQ.receiveOrder(message);

    verify(orderService, never()).createOrder(orderDTO);
  }

  @Test
  void receiveOrderWithIOException() throws IOException {
    Message message = mock(Message.class);
    when(message.getBody()).thenReturn("invalid-json".getBytes());
    when(objectMapper.readValue(message.getBody(), OrderDTO.class)).thenThrow(new IOException());

    assertThrows(AmqpRejectAndDontRequeueException.class, () -> consumerRabbitMQ.receiveOrder(message));
  }

  @Test
  void processFailedOrderSuccessfully() throws IOException {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setIdempotencyKey("unique-key");
    Message message = mock(Message.class);
    when(message.getBody()).thenReturn("{\"idempotencyKey\":\"unique-key\"}".getBytes());
    when(objectMapper.readValue(message.getBody(), OrderDTO.class)).thenReturn(orderDTO);
    when(orderService.existsByIdempotencyKey("unique-key")).thenReturn(false);

    consumerRabbitMQ.processFailedOrder(message);

    verify(orderService).createOrder(orderDTO);
  }

  @Test
  void processFailedOrderWithDuplicateIdempotencyKey() throws IOException {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setIdempotencyKey("unique-key");
    Message message = mock(Message.class);
    when(message.getBody()).thenReturn("{\"idempotencyKey\":\"unique-key\"}".getBytes());
    when(objectMapper.readValue(message.getBody(), OrderDTO.class)).thenReturn(orderDTO);
    when(orderService.existsByIdempotencyKey("unique-key")).thenReturn(true);

    consumerRabbitMQ.processFailedOrder(message);

    verify(orderService, never()).createOrder(orderDTO);
  }

  @Test
  void processFailedOrderWithIOException() throws IOException {
    Message message = mock(Message.class);
    when(message.getBody()).thenReturn("invalid-json".getBytes());
    when(objectMapper.readValue(message.getBody(), OrderDTO.class)).thenThrow(new IOException());

    consumerRabbitMQ.processFailedOrder(message);

    verify(orderService, never()).createOrder(any(OrderDTO.class));
  }
}