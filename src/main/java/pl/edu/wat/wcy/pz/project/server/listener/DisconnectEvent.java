package pl.edu.wat.wcy.pz.project.server.listener;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.entity.game.enumeration.GameStatus;
import pl.edu.wat.wcy.pz.project.server.mapper.TicTacToeGameMapper;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;

import java.util.List;

@AllArgsConstructor
@Component
public class DisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private TicTacToeGameRepository ticTacToeGameRepository;

    private SimpUserRegistry userRegistry;

    private SimpMessagingTemplate template;

    private TicTacToeGameMapper ticTacToeGameMapper;

    private Subscribers subscribers;

    private static final Logger LOGGER = LoggerFactory.getLogger(DisconnectEvent.class);

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        if (event.getUser() == null) {
            LOGGER.warn("Unauthenticated user - disconnect event.");
            return;
        }
        String username = event.getUser().getName();
        subscribers.removeSubscriptions(username);
        LOGGER.info("User: " + username + " disconnected.");

        updateGameState(username);
    }

    private void updateGameState(String username) {
        List<TicTacToeGame> games = ticTacToeGameRepository.findAllBySecondPlayer_UsernameAndGameStatus(username, GameStatus.WAITING_FOR_PLAYER);
        LOGGER.trace("Games to update (set second player to null): " + games.size());
        games.forEach(ticTacToeGame -> ticTacToeGame.setSecondPlayer(null));
        ticTacToeGameRepository.saveAll(games);

        games.forEach(ticTacToeGame -> {
            template.convertAndSend("/tictactoe/update", ticTacToeGameMapper.toDto(ticTacToeGame));
        });
    }

}
