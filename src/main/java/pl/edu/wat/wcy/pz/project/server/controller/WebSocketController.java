package pl.edu.wat.wcy.pz.project.server.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import pl.edu.wat.wcy.pz.project.server.dto.TicTacToeGameStateDTO;
import pl.edu.wat.wcy.pz.project.server.dto.response.Message;
import pl.edu.wat.wcy.pz.project.server.service.logic.TicTacToeLogic;

import java.security.Principal;
import java.util.Calendar;

@Controller
@AllArgsConstructor
public class WebSocketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);

    private final SimpMessagingTemplate template;

    private TicTacToeLogic ticTacToeLogic;

    @MessageMapping("/send/message/{gameId}")
    public void sendMessage(@DestinationVariable Integer gameId, String message, Principal principal) {
        LOGGER.trace("User send message. Game: " + gameId + ". User: " + principal.getName() + ". Message: " + message);

        Message messagetoSend = new Message(principal.getName(), message, Calendar.getInstance().getTime());
        this.template.convertAndSend("/chat/" + gameId, messagetoSend);
    }

    @MessageMapping("/send/move/{gameId}")
    public void sendMove(@DestinationVariable Long gameId, String message, Principal principal) throws InterruptedException {
        Integer fieldNumber = Integer.parseInt(message);
        LOGGER.trace("Move received. Game: " + gameId + ". Field: " + fieldNumber + ". User: " + principal.getName());

        TicTacToeGameStateDTO updatedGameState = ticTacToeLogic.updateGame(gameId, principal.getName(), fieldNumber);
        this.template.convertAndSend("/move/" + gameId, updatedGameState);

        if (updatedGameState.getUserTurn() == null) {
            LOGGER.trace("AI turn. Game: " + gameId);
            TicTacToeGameStateDTO gameStateAfterAIMove = ticTacToeLogic.createAIMove(updatedGameState);
            this.template.convertAndSend("/move/" + gameId, gameStateAfterAIMove);
        }
    }
}
