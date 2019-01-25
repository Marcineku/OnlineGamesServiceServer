package pl.edu.wat.wcy.pz.project.server.listener;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import pl.edu.wat.wcy.pz.project.server.entity.game.GameStatus;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
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

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        SimpUser user = userRegistry.getUser(event.getUser().getName());
        String username = event.getUser().getName();

        System.out.println("Disconnect " + username);
        List<TicTacToeGame> games = ticTacToeGameRepository.findAllBySecondPlayer_UsernameAndGameStatus(username, GameStatus.WAITING_FOR_PLAYER);

        games.forEach(ticTacToeGame -> ticTacToeGame.setSecondPlayer(null));
        ticTacToeGameRepository.saveAll(games);

        games.forEach(ticTacToeGame -> {
            template.convertAndSend("/tictactoe/update", ticTacToeGameMapper.toDto(ticTacToeGame));
        });
    }

}
