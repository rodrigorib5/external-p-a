package com.empresa.producta.service.amqp.impl;

import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.entity.OrderEntity;
import com.empresa.producta.service.OrderService;
import com.empresa.producta.service.amqp.AmqpConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConsumerRabbitMQ implements AmqpConsumer {

  private final ObjectMapper objectMapper;
  private final OrderService orderService;

  @RabbitListener(queues = "q.orders.message-collect")
  public void receiveOrder(Message message) {
    try {
      OrderDTO orderDTO = objectMapper.readValue(message.getBody(), OrderDTO.class);
      log.info("Order received: {}", new String(message.getBody()));

      if (!orderService.existsByIdempotencyKey(orderDTO.getIdempotencyKey())) {
        OrderEntity order = orderService.createOrder(orderDTO);
        log.info("Order created: {}", order);
      } else {
        log.warn("Duplicate order detected, ignoring. idempotencyKey: {}",
            orderDTO.getIdempotencyKey());
      }

    } catch (IOException e) {
      log.error("Error processing order: {}", new String(message.getBody()), e);
      throw new AmqpRejectAndDontRequeueException("Fatal error, sending to DLQ", e);
    }
  }

  @RabbitListener(queues = "dl.orders.message")
  public void processFailedOrder(Message message) {
    log.error("Processing failed order from DLQ: {}", new String(message.getBody()));

    try {
      OrderDTO orderDTO = objectMapper.readValue(message.getBody(), OrderDTO.class);
      if (!orderService.existsByIdempotencyKey(orderDTO.getIdempotencyKey())) {
        OrderEntity order = orderService.createOrder(orderDTO);
        log.info("Order reprocessed successfully: {}", order);
      } else {
        log.warn("Duplicate order detected in DLQ, skipping.");
      }
    } catch (IOException e) {
      log.error("Irrecoverable error processing order in DLQ: {}", new String(message.getBody()),
          e);
    }
  }
}
