package pl.edu.wat.wcy.pz.project.server.listener;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import pl.edu.wat.wcy.pz.project.server.entity.game.TicTacToeGame;
import pl.edu.wat.wcy.pz.project.server.mapper.TicTacToeGameMapper;
import pl.edu.wat.wcy.pz.project.server.repository.TicTacToeGameRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UnsubscribeListener implements ApplicationListener<SessionUnsubscribeEvent> {

    private TicTacToeGameRepository ticTacToeGameRepository;

    private SimpUserRegistry userRegistry;

    private SimpMessagingTemplate template;

    private TicTacToeGameMapper ticTacToeGameMapper;

    private Subscribers subscribers;

    private static final Logger LOGGER = LoggerFactory.getLogger(UnsubscribeListener.class);

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {

        if (event.getUser() == null) {
            LOGGER.error("Unauthenticated user - unsubscribe event.");
            return;
        }

        String username = event.getUser().getName();
        SimpUser user = userRegistry.getUser(username);

        if (user == null) {
            LOGGER.error("User with this name not exist in userRegistry.");
            return;
        }
        Set<SimpSession> sessions = user.getSessions();
        Set<SimpSubscription> subscriptions = new HashSet<>();
        sessions.forEach(simpSession -> subscriptions.addAll(simpSession.getSubscriptions()));

        System.out.println("Unubscribe:");
        subscriptions.forEach(simpSubscription -> {
            System.out.println("id " + simpSubscription.getId());
            System.out.println("destination " + simpSubscription.getDestination());
        });

        String unsubscribed = subscribers.updateAndReturnDifference(username, subscriptions.stream().map(SimpSubscription::getDestination).collect(Collectors.toSet()));

        LOGGER.info("Unsubscribe Event: " + username + ". Unsubscribe: ");
        LOGGER.info(unsubscribed);

        processUnsubscribedPath(unsubscribed, username);
    }

    private void processUnsubscribedPath(String unsubscribed, String username) {
        if (unsubscribed.contains("/chat/")) {
            Long gameId = getGameIdFromPath(unsubscribed);

            Optional<TicTacToeGame> gameOptional = ticTacToeGameRepository.findById(gameId);
            if (!gameOptional.isPresent()) {
                LOGGER.error("Game with this id not exist");
                throw new RuntimeException("Game with this id not exist");
            }
            TicTacToeGame game = gameOptional.get();

            checkIfSecondPlayerLeft(game, username);
        }

    }

    private void checkIfSecondPlayerLeft(TicTacToeGame game, String username) {
        if (game.getSecondPlayer() == null) {
            return;
        }
        if (username.equals(game.getSecondPlayer().getUsername())) {
            game.setSecondPlayer(null);
            ticTacToeGameRepository.save(game);
            template.convertAndSend("/tictactoe/update", ticTacToeGameMapper.toDto(game));
        }
    }

    private Long getGameIdFromPath(String unsubscribed) {
        String[] split = unsubscribed.split("/");
        if (!isNumeric(split[2])) {
            LOGGER.error("Invalid destination");
            throw new RuntimeException("Invalid destination");
        }
        return (long) Integer.parseInt(split[2]);
    }

    private boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
