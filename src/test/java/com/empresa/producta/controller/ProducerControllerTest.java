package com.empresa.producta.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.empresa.producta.dto.OrderDTO;
import com.empresa.producta.service.amqp.AmqpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class ProducerControllerTest {

  @Mock
  private AmqpService amqpService;

  @InjectMocks
  private ProducerController producerController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void sendOrderSuccessfully() {
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setStatus("waiting");

    ResponseEntity<Void> response = producerController.sendOrder(orderDTO);

    assertEquals(202, response.getStatusCodeValue());
    verify(amqpService).sendToConsumer(orderDTO);
  }
}