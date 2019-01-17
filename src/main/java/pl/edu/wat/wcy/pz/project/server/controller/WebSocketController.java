package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@AllArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/send/message/{gameId}")
    public void sendMessage(@DestinationVariable Integer gameId, String  message, Principal principal) throws InterruptedException {
        Thread.sleep(1000);  //todo
        System.out.println(principal.getName() + "<----");
        this.template.convertAndSend("/chat/" + gameId,
                new SimpleDateFormat("HH:mm:ss").format(new Date()) + "- " + message);
    }

    @MessageMapping("/send/move/{gameId}")
    public void sendMove(@DestinationVariable Integer gameId, String  message, Principal principal) throws InterruptedException {
        Thread.sleep(1000);  //todo
        System.out.println(principal.getName() + "<----");
        this.template.convertAndSend("/chat/" + gameId,
                new SimpleDateFormat("HH:mm:ss").format(new Date()) + "- " + message);
    }
}
