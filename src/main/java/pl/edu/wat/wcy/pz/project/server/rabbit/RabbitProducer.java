package pl.edu.wat.wcy.pz.project.server.rabbit;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;

@AllArgsConstructor
public class RabbitProducer {

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitme.routingkey}")
    private String routingKey;

    private AmqpTemplate amqpTemplate;

    public void sendToQueue(String text) {
        amqpTemplate.convertAndSend(exchange, routingKey, text);
        //todo
        System.out.println("Text sent to queue: " + text);
    }

}
