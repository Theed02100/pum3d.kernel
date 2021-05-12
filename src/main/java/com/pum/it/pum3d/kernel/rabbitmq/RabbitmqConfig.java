package com.pum.it.pum3d.kernel.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:rabbitmq.properties")
public class RabbitmqConfig {

  @Value("${exchangeNameForConsume}")
  private String exchangeNameForConsume;

  @Value("${incrementQueue}")
  private String incrementQueue;

  public String getExchangeNameForConsume() {
    return exchangeNameForConsume;
  }

  public String getIncrementQueue() {
    return incrementQueue;
  }

  /**
   * Création de la queue increment.
   *
   * @return la queue nouvellement créée
   */
  @Bean
  Queue incrementQueue() {
    return new Queue(incrementQueue, false);
  }

  /**
   * Crée le lien avec la queue increment.
   *
   * @return le Binding nouvellement créé
   */
  @Bean
  public Binding incrementBinding() {
    return new Binding(
        incrementQueue,
        Binding.DestinationType.QUEUE,
        exchangeNameForConsume,
        "*", null);
  }

  /**
   * Configure la connexion à Rabbit.
   *
   * @param connectionFactory
   *     contient les informations de connexion rabbit
   *
   * @return Une instance de SimpleRabbitListenerContainerFactory
   */
  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      final ConnectionFactory connectionFactory) {

    SimpleRabbitListenerContainerFactory factory =
        new SimpleRabbitListenerContainerFactory();

    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jsonMessageConverter());
    return factory;
  }

  /**
   * Crée un message converter pour gérer la traduction des messages rabbit.
   *
   * @return MessageConverter créé par la lib Jackson
   */
  @Bean
  MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter(
        new ObjectMapper().findAndRegisterModules());
  }
}
