package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import pl.edu.wat.wcy.pz.project.server.form.TicTacToeGameStateDTO;
import pl.edu.wat.wcy.pz.project.server.form.response.Message;
import pl.edu.wat.wcy.pz.project.server.service.logic.TicTacToeLogic;

import java.security.Principal;
import java.util.Calendar;

@Controller
@AllArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate template;

    private TicTacToeLogic ticTacToeLogic;

    @MessageMapping("/send/message/{gameId}")
    public void sendMessage(@DestinationVariable Integer gameId, String message, Principal principal) throws InterruptedException {
        System.out.println(principal.getName() + "<----");
        Message messagetoSend = new Message(principal.getName(), message, Calendar.getInstance().getTime());
        this.template.convertAndSend("/chat/" + gameId, messagetoSend);
    }

    @MessageMapping("/send/move/{gameId}")
    public void sendMove(@DestinationVariable Long gameId, String message, Principal principal) throws InterruptedException {
        Integer fieldNumber = Integer.parseInt(message);
        TicTacToeGameStateDTO updatedGameState = ticTacToeLogic.updateGame(gameId, principal.getName(), fieldNumber);

        System.out.println(principal.getName() + "<----");
        this.template.convertAndSend("/move/" + gameId,
                updatedGameState);
    }

//    @MessageMapping("")
//    public void sendTicTacToeList() {
//        this.template.convertAndSend("/tictactoe");
//    }
}
