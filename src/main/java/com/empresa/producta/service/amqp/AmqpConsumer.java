package com.empresa.producta.service.amqp;

import org.springframework.amqp.core.Message;

public interface AmqpConsumer {

  void receiveOrder(Message message);

  void processFailedOrder(Message message);
}
