package com.empresa.producta.service.amqp.impl;

import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.service.amqp.AmqpProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class RabbitMQServiceImplTest {

  @Mock
  private AmqpProducer<OrderDTO> amqpProducer;

  @InjectMocks
  private RabbitMQServiceImpl rabbitMQService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void sendToConsumerSuccessfully() {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setIdempotencyKey("unique-key");

    rabbitMQService.sendToConsumer(orderDTO);

    verify(amqpProducer).producer(orderDTO);
  }
}