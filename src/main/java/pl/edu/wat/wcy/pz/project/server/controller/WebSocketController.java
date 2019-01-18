package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import pl.edu.wat.wcy.pz.project.server.form.response.Message;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
@AllArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/send/message/{gameId}")
    public void sendMessage(@DestinationVariable Integer gameId, String message, Principal principal) throws InterruptedException {
        System.out.println(principal.getName() + "<----");
        Message messagetoSend = new Message(principal.getName(), message, Calendar.getInstance().getTime());
        this.template.convertAndSend("/chat/" + gameId, messagetoSend);
    }

    @MessageMapping("/send/move/{gameId}")
    public void sendMove(@DestinationVariable Integer gameId, String message, Principal principal) throws InterruptedException {
        Thread.sleep(1000);  //todo
        System.out.println(principal.getName() + "<----");
        this.template.convertAndSend("/move/" + gameId,
                new SimpleDateFormat("HH:mm:ss").format(new Date()) + "- " + message);
    }
}
