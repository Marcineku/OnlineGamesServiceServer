package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@AllArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/send/message")
    public void send(String  message) throws InterruptedException {
        Thread.sleep(1000);
        //this.template.convertAndSend("/chat",
        //        new SimpleDateFormat("HH:mm:ss").format(new Date()) + "- " + message);
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.template.convertAndSend("/chat",
                new SimpleDateFormat("HH:mm:ss").format(new Date()) + "- " + principal.getUsername());
    }

}
