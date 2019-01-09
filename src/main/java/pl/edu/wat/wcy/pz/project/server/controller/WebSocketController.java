package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import pl.edu.wat.wcy.pz.project.server.form.response.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@AllArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/send/message")
    public void send(Message message) throws InterruptedException {
        Thread.sleep(1000);
        this.template.convertAndSend("/chat",
                new SimpleDateFormat("HH:mm:ss").format(new Date()) + "- " + message.getMessage());
    }

}
