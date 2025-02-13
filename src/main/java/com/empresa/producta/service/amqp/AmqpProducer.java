package com.empresa.producta.service.amqp;

public interface AmqpProducer<T> {

  void producer(T t);
}
