package com.empresa.producta.service.amqp.impl;

import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.service.amqp.AmqpProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProducerRabbitMQ implements AmqpProducer<OrderDTO> {

  private final RabbitTemplate rabbitTemplate;
  private final Queue messageCollectQueue;
  private final ObjectMapper objectMapper;

  @Override
  public void producer(OrderDTO orderRequestDTO) {
    String queueName = messageCollectQueue.getName();

    try {
      String messageJSON = objectMapper.writeValueAsString(orderRequestDTO);
      rabbitTemplate.convertAndSend(queueName, messageJSON);
      log.info("Message sent to queue [{}]: {}", queueName, messageJSON);
    } catch (JsonProcessingException ex) {
      log.error("Error serializing message for queue [{}]: {}", queueName, ex.getMessage(), ex);
      throw new AmqpRejectAndDontRequeueException("Failed to serialize message", ex);
    }
  }
}
