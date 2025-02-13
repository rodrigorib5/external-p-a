package com.empresa.producta.service.amqp.impl;

import com.empresa.producta.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProducerRabbitMQTest {

  @Mock
  private RabbitTemplate rabbitTemplate;

  @Mock
  private Queue messageCollectQueue;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private ProducerRabbitMQ producerRabbitMQ;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void producerSuccessfully() throws JsonProcessingException {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setIdempotencyKey("unique-key");
    String queueName = "test-queue";
    String messageJSON = "{\"idempotencyKey\":\"unique-key\"}";

    when(messageCollectQueue.getName()).thenReturn(queueName);
    when(objectMapper.writeValueAsString(orderDTO)).thenReturn(messageJSON);

    producerRabbitMQ.producer(orderDTO);

    verify(rabbitTemplate).convertAndSend(queueName, messageJSON);
  }

  @Test
  void producerWithJsonProcessingException() throws JsonProcessingException {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setIdempotencyKey("unique-key");
    String queueName = "test-queue";

    when(messageCollectQueue.getName()).thenReturn(queueName);
    when(objectMapper.writeValueAsString(orderDTO)).thenThrow(new JsonProcessingException("error") {});

    assertThrows(AmqpRejectAndDontRequeueException.class, () -> producerRabbitMQ.producer(orderDTO));
  }
}