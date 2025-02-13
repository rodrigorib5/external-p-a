package com.empresa.producta.service.amqp;


import com.empresa.producta.dto.OrderDTO;

public interface AmqpService {

  void sendToConsumer(OrderDTO order);
}
