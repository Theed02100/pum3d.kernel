package com.pum.it.pum3d.kernel.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pum.it.pum3d.kernel.dao.IncrementDao;
import com.pum.it.pum3d.kernel.model.entity.Increment;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListener {

  static final Logger LOGGER = LogManager.getLogger(RabbitMqListener.class);
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  @Autowired
  private IncrementDao incrementDao;

  /**
   * Listener de la queue increment.
   * Traduit le message de rabbit (un Integer) et le set dans l'entry de la bdd.
   *
   * @param message le message reçu de rabbit
   */
  @RabbitListener(queues = "increment.queue")
  public void consommeIncrement(Message message) {
    if (message.getBody() != null) {
      try {
        Integer count = OBJECT_MAPPER.readValue(message.getBody(), Integer.class);
        Increment increment = null;
        if (incrementDao.count() != 0) {
          increment = incrementDao.findAll().get(0);
        } else {
          increment = new Increment();
        }
        increment.setCount(count);
        incrementDao.save(increment);
        LOGGER.debug("Set count via rabbitMQ. New value : " + count);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
