package pl.edu.wat.wcy.pz.project.server.listener;

import lombok.AllArgsConstructor;
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

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {

        SimpUser user = userRegistry.getUser(event.getUser().getName());
        String username = event.getUser().getName();
        Set<SimpSession> sessions = user.getSessions();
        Set<SimpSubscription> subscriptions = new HashSet<>();
        sessions.forEach(simpSession -> subscriptions.addAll(simpSession.getSubscriptions()));

        System.out.println("Unubscribe:");
        subscriptions.forEach(simpSubscription -> {
            System.out.println("id " + simpSubscription.getId());
            System.out.println("destination " + simpSubscription.getDestination());
        });

        String unsubscribed = subscribers.updateAndReturnDifference(username, subscriptions.stream().map(SimpSubscription::getDestination).collect(Collectors.toSet()));

        System.out.println(unsubscribed + " <-- unsubscribed STUFF");

        if (unsubscribed.contains("/chat/")) {

            String[] split = unsubscribed.split("/");
            if (!isNumeric(split[2]))
                throw new RuntimeException("Invalid destination");
            Long gameId = (long) Integer.parseInt(split[2]);
            Optional<TicTacToeGame> gameOptional = ticTacToeGameRepository.findById(gameId);
            if (!gameOptional.isPresent())
                throw new RuntimeException("Game with this id not exist");
            TicTacToeGame game = gameOptional.get();

            if (game.getSecondPlayer() == null){
                return;
            }
            if (username.equals(game.getSecondPlayer().getUsername())) {
                game.setSecondPlayer(null);
                ticTacToeGameRepository.save(game);
                template.convertAndSend("/tictactoe/update", ticTacToeGameMapper.toDto(game));
            }
        }
    }

    private boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
