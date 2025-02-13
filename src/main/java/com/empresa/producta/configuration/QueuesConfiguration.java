package com.empresa.producta.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueuesConfiguration {

  private static final String ARG_HEADER_DLX_ROUTING_KEY = "x-dead-letter-routing-key";
  private static final String ARG_HEADER_DLX_EXCHANGE = "x-dead-letter-exchange";

  @Value("${spring.rabbitmq.exchange}")
  private String ordersMessageExchangeName;

  @Value("${spring.rabbitmq.queue.agenda.message.collect}")
  private String messageCollectQueueName;

  @Value("${spring.rabbitmq.queue.deadLetter}")
  private String deadLetterName;

  @Bean
  public Queue messageCollectQueue() {
    return QueueBuilder.durable(this.messageCollectQueueName)
        .withArgument(ARG_HEADER_DLX_EXCHANGE, this.ordersMessageExchangeName)
        .withArgument(ARG_HEADER_DLX_ROUTING_KEY, this.deadLetterName)
        .build();
  }

  @Bean
  DirectExchange orderMessageExchange() {
    return new DirectExchange(this.ordersMessageExchangeName);
  }

  @Bean
  public Queue deadLetterQueue() {
    return QueueBuilder.durable(this.deadLetterName)
        .withArgument(ARG_HEADER_DLX_ROUTING_KEY, this.deadLetterName)
        .withArgument(ARG_HEADER_DLX_EXCHANGE, this.ordersMessageExchangeName)
        .build();
  }

  @Bean
  Binding deadLetterQueueBinding() {
    return BindingBuilder.bind(this.deadLetterQueue()).to(this.orderMessageExchange()).with(this.deadLetterName);
  }
}
