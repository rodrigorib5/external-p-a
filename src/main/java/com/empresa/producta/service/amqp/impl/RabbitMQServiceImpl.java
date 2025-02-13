package com.empresa.producta.service.amqp.impl;

import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.service.amqp.AmqpProducer;
import com.empresa.producta.service.amqp.AmqpService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQServiceImpl implements AmqpService {

  private final AmqpProducer<OrderDTO> amqp;

  @Override
  @Async
  public void sendToConsumer(OrderDTO order) {
    this.amqp.producer(order);
  }
}
