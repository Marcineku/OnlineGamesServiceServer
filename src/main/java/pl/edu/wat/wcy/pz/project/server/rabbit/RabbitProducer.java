package pl.edu.wat.wcy.pz.project.server.rabbit;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.edu.wat.wcy.pz.project.server.form.EmailDTO;

@AllArgsConstructor
@Component
@NoArgsConstructor
public class RabbitProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitProducer.class);

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendToQueue(EmailDTO dto) {
        amqpTemplate.convertAndSend(exchange, routingKey, dto);
        LOGGER.info("Message sent to queue");
    }

}
